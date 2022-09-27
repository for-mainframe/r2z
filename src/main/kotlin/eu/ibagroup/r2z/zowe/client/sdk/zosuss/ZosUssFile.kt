// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosuss

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

class ZosUssFile (
    var connection: ZOSConnection,
    var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {
    init {
        connection.checkConnection()
    }

    var response: Response<*>? = null

    /**
     * Writes plain text to USS file. Creates new if not exist
     *
     * @param filePath path of the file or directory (e.g. u/jiahj/text.txt)
     * @param text plain text to be written to file
     * @return http response object
     * @throws Exception error processing request
     */
    fun writeToFile(filePath: String, text: ByteArray): Response<*> {
        val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
        val dataApi = buildApi<DataAPI>(url, httpClient)
        val call = dataApi.writeToUssFile(
            authorizationToken = Credentials.basic(connection.user, connection.password),
            filePath = FilePath(filePath),
            body = text
        )
        response = call.execute()
        if (response?.isSuccessful != true) {
            throw Exception(response?.errorBody()?.string())
        }
        return response ?: throw Exception("No response returned")
    }
}
