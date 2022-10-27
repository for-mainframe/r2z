// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosuss

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosuss.input.UssListParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Query
import java.io.InputStream

class ZosUssFileList (
    var connection: ZOSConnection,
    var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {
    init {
        connection.checkConnection()
    }

    var response: Response<*>? = null

    /**
     * List the files and directories of a UNIX file path
     *
     * @param filePath path of the file (e.g. u/jiahj/)
     * @param params USS list fili parameters
     * @return http response object
     * @throws Exception error processing request
     */
    fun listFiles(filePath: String, params: UssListParams): UssFilesList {
        val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
        val dataApi = buildApi<DataAPI>(url, httpClient)
        val call = dataApi.listUssPath(
            authorizationToken = Credentials.basic(connection.user, connection.password),
            path = filePath,
            xIBMMaxItems = params.limit,
            xIBMLstat = params.lstat,
            depth = params.depth,
            fileSystem = params.fileSystem,
            followSymlinks = params.followSymlinks,
            group = params.group,
            mtime = params.mtime,
            name = params.name,
            size = params.size,
            perm = params.perm,
            type = params.type,
            user = params.user
        )
        response = call.execute()
        if (response?.isSuccessful != true) {
            throw Exception(response?.errorBody()?.string())
        }
        return response?.body() as UssFilesList? ?: throw Exception("No body returned")
    }
}
