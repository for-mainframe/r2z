// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosconsole

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * Issue MVS Console commands by using a system console
 */
class IssueCommand(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Issue an MVS console command, returns "raw" z/OSMF response
   *
   * @param consoleName string name of the mvs console that is used to issue the command
   * @param commandParams synchronous console issue parameters, see [IssueRequestBody] object
   * @return command response on resolve, see [IssueResponse] object
   * @throws Exception processing error
   */
  fun issueCommon(consoleName: String, commandParams: IssueRequestBody): IssueResponse {
    if (consoleName.isEmpty()) {
      throw Exception("consoleName not specified")
    }
    if (commandParams.cmd.isEmpty()) {
      throw Exception("command not specified")
    }

    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val consoleApi = buildApi<ConsoleAPI>(url, httpClient)
    val call = consoleApi.issueCommand(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      consoleName = consoleName,
      body = commandParams
    )

    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as IssueResponse? ?: throw Exception("No body returned")
  }

  /**
   * Issue an MVS console command in default console, returns "raw" z/OSMF response
   *
   * @param commandParams synchronous console issue parameters, see [IssueRequestBody] object
   * @return command response on resolve, see [IssueResponse] object
   * @throws Exception processing error
   */
  fun issueDefConsoleCommon(commandParams: IssueRequestBody): IssueResponse {
    return issueCommon("defcn", commandParams)
  }

  /**
   * Issue an MVS console command done synchronously - meaning solicited (direct command responses) are gathered
   * immediately after the command is issued. However, after (according to the z/OSMF REST API documentation)
   * approximately 3 seconds the response will be returned.
   *
   * @param params console issue parameters, see [IssueRequestBody] object
   * @return command response on resolve, see ConsoleResponse object
   * @throws Exception processing error
   */
  fun issue(params: IssueRequestBody): ConsoleResponse {
    val resp = issueCommon("defcn", params)
    val consoleResponse = ConsoleResponse(
      zosmfResponse = resp,
      success = true,
      keywordDetected = resp.solKeyDetected?.isNotEmpty() == true,
      lastResponseKey = resp.cmdResponseKey,
      cmdResponseUrl = resp.cmdResponseUrl
    )

    if (resp.cmdResponse != null) {
      val responseValue = resp.cmdResponse.replace('\r', '\n')
      consoleResponse.commandResponse = responseValue

      if (responseValue.isNotEmpty() && (responseValue.indexOf("\n") != responseValue.length - 1)) {
        consoleResponse.commandResponse = responseValue + "\n"
      }
    }

    return consoleResponse
  }

  /**
   * Simple issue console command method. Does not accept parameters, so all defaults on the z/OSMF API are taken.
   *
   * @param command string command to issue
   * @return command response on resolve, see [ConsoleResponse] object
   * @throws Exception processing error
   */
  fun issueSimple(command: String): ConsoleResponse {
    return issue(IssueRequestBody(cmd = command))
  }

}