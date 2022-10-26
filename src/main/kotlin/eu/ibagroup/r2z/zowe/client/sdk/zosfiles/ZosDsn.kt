// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosfiles

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input.ListParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * ZosDsn class that provides CRUD operations on Datasets
 */
class ZosDsn(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Retrieves the information about a Dataset.
   *
   * @param dataSetName sequential or partition dataset (e.g. 'DATASET.LIB')
   * @return dataset object
   * @see Dataset
   * @throws Exception error processing request
   */
  fun getDatasetInfo(dataSetName: String): Dataset {
    val emptyDataSet = Dataset(dataSetName)
    val tokens: List<String> = dataSetName.split(".")
    val length = tokens.size - 1
    if (1 >= length) {
      return emptyDataSet
    }
    val str = StringBuilder()
    for (i in 0 until length) {
      str.append(tokens[i])
      str.append(".")
    }
    var dataSetSearchStr = str.toString()
    dataSetSearchStr = dataSetSearchStr.substring(0, str.length - 1)
    val zosDsnList = ZosDsnList(connection, httpClient)
    val params = ListParams(
      attribute = XIBMAttr.Type.BASE
    )
    val dsLst: DataSetsList = zosDsnList.listDsn(dataSetSearchStr, params)
    val dataSet: Dataset? = dsLst.items.filter { el -> el.name.contains(dataSetName) }.getOrNull(0)
    return  dataSet ?: emptyDataSet
  }

  /**
   * Delete a dataset
   *
   * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
   * @return http response object
   * @throws Exception error processing request
   */
  fun deleteDsn(dataSetName: String): Response<*> {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val dataApi = buildApi<DataAPI>(url, httpClient)
    val call = dataApi.deleteDataset(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      datasetName = dataSetName
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response ?: throw Exception("No response returned")
  }

  /**
   * Delete a dataset member
   *
   * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
   * @param member name of member to delete
   * @return http response object
   * @throws Exception error processing request
   */
  fun deleteDsn(dataSetName: String, member: String): Response<*> {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val dataApi = buildApi<DataAPI>(url, httpClient)
    val call = dataApi.deleteDatasetMember(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      datasetName = dataSetName,
      memberName = member
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response ?: throw Exception("No response returned")
  }

  /**
   * Replaces the content of an existing sequential data set with new content.
   *
   * @param dataSetName sequential dataset (e.g. 'DATASET.LIB')
   * @param content new content
   * @return http response object
   * @throws Exception error processing request
   */
  fun writeDsn(dataSetName: String, content: ByteArray): Response<*> {
    val baseUrl = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val dataApi = buildApiWithBytesConverter<DataAPI>(baseUrl, httpClient)
    val call = dataApi.writeToDataset(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      datasetName = dataSetName,
      content = content
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response ?: throw Exception("No response returned")
  }

  /**
   * Replaces the content of a member of a partitioned data set (PDS or PDSE) with new content.
   * A new dataset member will be created if the specified dataset member does not exist.
   *
   * @param dataSetName dataset name of where the member is located (e.g. 'DATASET.LIB')
   * @param member name of member to add new content
   * @param content new content
   * @return http response object
   * @throws Exception error processing request
   */
  fun writeDsn(dataSetName: String, member: String, content: ByteArray): Response<*> {
    val baseUrl = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val dataApi = buildApiWithBytesConverter<DataAPI>(baseUrl, httpClient)
    val call = dataApi.writeToDatasetMember(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      datasetName = dataSetName,
      content = content,
      memberName = member
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response ?: throw Exception("No response returned")
  }

  /**
   * Creates a new dataset with specified parameters
   *
   * @param dataSetName name of a dataset to create (e.g. 'DATASET.LIB')
   * @param params create dataset parameters, see CreateDataset object
   * @return http response object
   * @throws Exception error processing request
   */
  fun createDsn(dataSetName: String, params: CreateDataset): Response<*> {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val dataApi = buildApi<DataAPI>(url, httpClient)
    val call = dataApi.createDataset(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      datasetName = dataSetName,
      body = params
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response ?: throw Exception("No response returned")
  }
}
