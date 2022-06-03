// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosfiles

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input.ListParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * ZosDsnList class that provides Dataset member list function
 *
 * @property connection connection information, see ZOSConnection object
 * @property httpClient okHttpClient
 */
class ZosDsnList(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Get a list of Dataset names
   *
   * @param dataSetName name of a dataset
   * @param params list parameters, see ListParams object
   * @return A String list of Dataset names
   * @throws Exception error processing request
   */
  fun listDsn(datasetName: String, listParams: ListParams): DataSetsList {
    val baseUrl = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val dataApi = buildApi<DataAPI>(baseUrl, httpClient)
    val call = dataApi.listDataSets(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      xIBMAttr = XIBMAttr(listParams.attribute, listParams.returnTotalRows),
      xIBMMaxItems = listParams.maxLength ?: 0,
      xIBMResponseTimeout = listParams.responseTimeout,
      dsLevel = datasetName,
      volser = listParams.volume,
      start = listParams.start
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as DataSetsList? ?: throw Exception("No body returned")
  }

  /**
   * Get a list of members from a Dataset
   *
   * @param datasetName name of a dataset
   * @param listParams list parameters, see ListParams object
   * @return list of member names
   * @throws Exception error processing request
   */
  fun listDsnMembers(datasetName: String, listParams: ListParams): MembersList {
    val baseUrl = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val dataApi = buildApi<DataAPI>(baseUrl, httpClient)
    val call = dataApi.listDatasetMembers(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      xIBMAttr = XIBMAttr(listParams.attribute, listParams.returnTotalRows),
      xIBMMaxItems = listParams.maxLength ?: 0,
      xIBMMigratedRecall = MigratedRecall.valueOf(listParams.recall ?: "WAIT"),
      datasetName = datasetName,
      start = listParams.start ,
      pattern = listParams.pattern
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as MembersList? ?: throw Exception("No body returned")
  }
}
