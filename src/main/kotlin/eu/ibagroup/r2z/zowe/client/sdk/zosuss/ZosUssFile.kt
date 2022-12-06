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
     * Deletes USS file
     *
     * @param filePath path of the file or directory (e.g. u/jiahj/text.txt)
     * @return http response object
     * @throws Exception error processing request
     */
    fun deleteFile(filePath: String): Response<*> {
        val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
        val dataApi = buildApi<DataAPI>(url, httpClient)
        val call = dataApi.deleteUssFile(
            authorizationToken = Credentials.basic(connection.user, connection.password),
            filePath = FilePath(filePath)
        )
        response = call.execute()
        if (response?.isSuccessful != true) {
            throw Exception(response?.errorBody()?.string())
        }
        return response ?: throw Exception("No response returned")
    }

    /**
     * Creates a new file or directory with specified parameters
     *
     * @param filePath path of the file or directory (e.g. u/jiahj/text.txt)
     * @param params create USS file parameters, see CreateUssFile class
     * @return http response object
     * @throws Exception error processing request
     */
    fun createFile(filePath: String, params: CreateUssFile): Response<*> {
        val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
        val dataApi = buildApi<DataAPI>(url, httpClient)
        val call = dataApi.createUssFile(
            authorizationToken = Credentials.basic(connection.user, connection.password),
            filePath = FilePath(filePath),
            body = params
            )
        response = call.execute()
        if (response?.isSuccessful != true) {
            throw Exception(response?.errorBody()?.string())
        }
        return response ?: throw Exception("No response returned")
    }

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

    /**
     * Writes to USS binary file. Creates new if not exist
     *
     * @param filePath path of the file or directory (e.g. u/jiahj/text.txt)
     * @param inputFile file to be written to
     * @return http response object
     * @throws Exception error processing request
     */
    fun writeToFileBin(filePath: String, inputFile: ByteArray): Response<*> {
        val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
        val dataApi = buildApi<DataAPI>(url, httpClient)
        val call = dataApi.writeToUssFile(
            authorizationToken = Credentials.basic(connection.user, connection.password),
            filePath = FilePath(filePath),
            body = inputFile,
            xIBMDataType = XIBMDataType(XIBMDataType.Type.BINARY)
        )
        response = call.execute()
        if (response?.isSuccessful != true) {
            throw Exception(response?.errorBody()?.string())
        }
        return response ?: throw Exception("No response returned")
    }
}
