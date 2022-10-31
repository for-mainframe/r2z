// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zostso

import eu.ibagroup.r2z.TsoResponse
import eu.ibagroup.r2z.UnsafeOkHttpClient
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zostso.input.StartTsoParams
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * Class to handle issue command to TSO
 */
class IssueTso(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * API method to start a TSO address space with provided parameters, issue a command,
   * collect responses until prompt is reached, and terminate the address space.
   *
   * @param accountNumber accounting info for Jobs
   * @param command command text to issue to the TSO address space.
   * @param startParams start tso parameters, see startParams object
   * @return issue tso response, see IssueResponse object
   * @throws Exception error executing command
   */
  fun issueTsoCommand(accountNumber: String, command: String, startParams: StartTsoParams): IssueResponse {
    if (accountNumber.isEmpty()) {
      throw Exception("accountNumber not specified")
    }
    if (command.isEmpty()) {
      throw Exception("command not specified")
    }

    // first stage open tso servlet session to use for our tso command processing
    val startTso = StartTso(connection, httpClient)
    val startResponse = startTso.start(accountNumber, startParams)

    if (!startResponse.success) {
      throw Exception("TSO address space failed to start. Error: ${startResponse.failureResponse ?: "Unknown error"}")
    }

    val zosmfTsoResponses = mutableListOf<TsoResponse>()
    zosmfTsoResponses.add(startResponse.tsoResponse ?: throw Exception("no zosmf start tso response"))
    val servletKey = startResponse.servletKey

    // second stage send command to tso servlet session created in first stage and collect all tso responses
    val sendTso = SendTso(connection, httpClient)
    val sendResponse = sendTso.sendDataToTSOCollect(servletKey, command)

    zosmfTsoResponses.addAll(sendResponse.tsoResponses)

    // lastly save the command response to our issueResponse reference
    val stopTso = StopTso(connection, httpClient)
    val stopResponse = stopTso.stop(servletKey)

    return IssueResponse(
      success = sendResponse.success,
      startResponse = startResponse,
      zosmfResponses = zosmfTsoResponses,
      commandResponses = sendResponse.commandResponse ?: throw Exception("error getting command response"),
      stopResponse = stopResponse
    )
  }

  /**
   * API method to start a TSO address space, issue a command, collect responses until prompt is reached, and
   * terminate the address space.
   *
   * @param accountNumber accounting info for Jobs
   * @param command command text to issue to the TSO address space.
   * @return issue tso response, see IssueResponse object
   * @throws Exception error executing command
   */
  fun issueTsoCommand(accountNumber: String, command: String): IssueResponse {
    return issueTsoCommand(accountNumber, command, StartTsoParams())
  }

}