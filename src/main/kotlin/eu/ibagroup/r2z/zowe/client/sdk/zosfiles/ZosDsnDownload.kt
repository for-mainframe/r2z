// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosfiles

import eu.ibagroup.r2z.DataAPI
import eu.ibagroup.r2z.UnsafeOkHttpClient
import eu.ibagroup.r2z.buildApi
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input.DownloadParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.InputStream

/**
 * ZosDsnDownload class that provides download DataSet function
 */
class ZosDsnDownload (
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Downloads a sequential dataset or dataset member content
   *
   * @param datasetName name of a sequential dataset e.g. DATASET.SEQ.DATA
   *                    or a dataset member e.g. DATASET.LIB(MEMBER))
   * @param params      download params parameters, see DownloadParams
   * @return a content stream
   * @throws Exception error processing request
   */
  fun downloadDsn(datasetName: String, params: DownloadParams): InputStream {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val dataApi = buildApi<DataAPI>(url, httpClient)
    val call = if (params.volume != null) {
      dataApi.retrieveDatasetContent(
        authorizationToken = Credentials.basic(connection.user, connection.password),
        datasetName = datasetName,
        volser = params.volume,
        xIBMReturnEtag = params.returnEtag
      )
    } else {
      dataApi.retrieveDatasetContent(
        authorizationToken = Credentials.basic(connection.user, connection.password),
        datasetName = datasetName
      )
    }
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return (response?.body() as ResponseBody).byteStream() ?: throw Exception("No stream returned")
  }

}