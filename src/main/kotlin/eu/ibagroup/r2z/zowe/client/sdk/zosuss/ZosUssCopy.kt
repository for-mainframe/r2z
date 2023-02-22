// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosuss

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

class ZosUssCopy (
    var connection: ZOSConnection,
    var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {
    init {
        connection.checkConnection()
    }

    var response: Response<*>? = null

    /**
     * Copies USS file
     *
     * @param filePath path of the file or directory (e.g. u/jiahj/text.txt)
     * @param destPath path where to copy the file
     * @param replace if true file in the target destination will be overwritten
     * @return http response object
     * @throws Exception error processing request
     */
    fun copy(filePath: String, destPath: String, replace: Boolean): Response<*> {
        val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
        val dataApi = buildApi<DataAPI>(url, httpClient)
        val call = dataApi.copyUssFile(
            authorizationToken = Credentials.basic(connection.user, connection.password),
            body = CopyDataUSS.CopyFromFileOrDir(
                from = filePath,
                overwrite = replace
            ),
            filePath = FilePath(destPath)
        )
        response = call.execute()
        if (response?.isSuccessful != true) {
            throw Exception(response?.errorBody()?.string())
        }
        return response ?: throw Exception("No response returned")
    }

    /**
     * Copies USS file to sequential dataset
     *
     * @param filePath path of the file or directory (e.g. u/jiahj/text.txt)
     * @param dsn dataset where to copy the file
     * @param replace if true information in the target dataset will be replaced
     * @return http response object
     * @throws Exception error processing request
     */
    fun copyToDS(filePath: String, dsn: String, replace: Boolean): Response<*> {
        val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
        val dataApi = buildApi<DataAPI>(url, httpClient)
        val call = dataApi.copyToDatasetFromUss(
            authorizationToken = Credentials.basic(connection.user, connection.password),
            body = CopyDataZOS.CopyFromFile(
                file = CopyDataZOS.CopyFromFile.File(
                    fileName = filePath
                ),
                replace = replace
            ),
            toDatasetName = dsn
        )
        response = call.execute()
        if (response?.isSuccessful != true) {
            throw Exception(response?.errorBody()?.string())
        }
        return response ?: throw Exception("No response returned")
    }

    /**
     * Copies USS file to dataset member
     *
     * @param filePath path of the file or directory (e.g. u/jiahj/text.txt)
     * @param dsn dataset where to copy the file
     * @param member dataset member where to copy the file
     * @param replace if true information in the target member will be replaced
     * @return http response object
     * @throws Exception error processing request
     */
    fun copyToMember(filePath: String, dsn: String, member: String, replace: Boolean): Response<*> {
        val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
        val dataApi = buildApi<DataAPI>(url, httpClient)
        val call = dataApi.copyToDatasetMemberFromUssFile(
            authorizationToken = Credentials.basic(connection.user, connection.password),
            body = CopyDataZOS.CopyFromFile(
                file = CopyDataZOS.CopyFromFile.File(
                    fileName = filePath
                ),
                replace = replace
            ),
            toDatasetName = dsn,
            memberName = member
        )
        response = call.execute()
        if (response?.isSuccessful != true) {
            throw Exception(response?.errorBody()?.string())
        }
        return response ?: throw Exception("No response returned")
    }

}
