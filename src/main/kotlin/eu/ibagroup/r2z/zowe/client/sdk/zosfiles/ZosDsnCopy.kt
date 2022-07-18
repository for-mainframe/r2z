// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosfiles

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input.CopyParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * Provides copy dataset and dataset member functionality
 */
class ZosDsnCopy (
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Copy dataset or dataset member
   *
   * @param params contains copy dataset parameters
   * @return http response object
   * @throws Exception error processing copy request
   */
  fun copy(params: CopyParams): Response<*> {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val dataApi = buildApi<DataAPI>(url, httpClient)
    val call = if (params.toVolser != null) {
      dataApi.copyToDataset(
        authorizationToken = Credentials.basic(connection.user, connection.password),
        body = CopyDataZOS.CopyFromDataset(
          dataset = CopyDataZOS.CopyFromDataset.Dataset(
            datasetName = params.fromDataSet,
            memberName = if (params.copyAllMembers) ALL_MEMBERS else null,
            volumeSerial = params.fromVolser
          ),
          replace = params.replace
        ),
        toVolser = params.toVolser,
        toDatasetName = params.toDataSet
      )
    } else {
      dataApi.copyToDataset(
        authorizationToken = Credentials.basic(connection.user, connection.password),
        body = CopyDataZOS.CopyFromDataset(
          dataset = CopyDataZOS.CopyFromDataset.Dataset(
            datasetName = params.fromDataSet,
            memberName = if (params.copyAllMembers) ALL_MEMBERS else null
          ),
          replace = params.replace
        ),
        toDatasetName = params.toDataSet
      )
    }
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response ?: throw Exception("No response returned")
  }

  /**
   * This copy method allows the following copy operations:
   * - sequential dataset to sequential dataset
   * - sequential dataset to partition dataset member
   * - partition dataset member to partition dataset member
   * - partition dataset member to partition dataset non-existing member
   * - partition dataset member to sequential dataset
   *
   * If copyAllMembers parameter value sent as true it will perform a copy of all
   * members in source partition dataset to another partition dataset.
   *
   * @param fromDataSetName is a name of source dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
   * @param toDataSetName is a name of target dataset (e.g. 'TARGET.DATASET' or 'TARGET.DATASET(MEMBER)')
   * @param replace if true members in the target dataset are replaced
   * @param copyAllMembers if true copy all members in source partition dataset specified
   * @return http response object
   * @throws Exception error processing copy request
   */
  @Throws(Exception::class)
  fun copy(fromDataset: String, toDataset: String, replace: Boolean, copyAllMembers: Boolean) : Response<*> {
    return copy(CopyParams(fromDataSet = fromDataset, toDataSet = toDataset, replace = replace, copyAllMembers = copyAllMembers))
  }

}