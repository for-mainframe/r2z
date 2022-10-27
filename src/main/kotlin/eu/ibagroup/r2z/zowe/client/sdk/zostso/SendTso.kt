// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zostso

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zostso.input.SendTsoParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response
import kotlin.jvm.Throws

/**
 * Class to handle sending data to TSO
 */
class SendTso(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Create Response
   *
   * @param responses responses from CollectedResponses object
   * @return SendResponse, see [SendResponse]
   */
  @Throws(Exception::class)
  private fun createResponse(responses: CollectedResponses): SendResponse {
    return SendResponse(
      true,
      responses.tsos,
      responses.messages ?: throw Exception("no responses messages exist")
    )
  }

  /**
   * Collects responses from address space until it reaches prompt
   *
   * @param tsoResponse object from first Tso response from witch responses are needed, see [TsoResponse]
   * @return CollectedResponses response object, see [CollectedResponses]
   * @throws Exception error executing command
   */
  @Throws(Exception::class)
  fun getAllResponses(tsoResponse: TsoResponse): CollectedResponses {
    var tso = tsoResponse
    var done = false
    val messages = StringBuilder()
    val tsos = mutableListOf<TsoResponse>()
    tsos.add(tso)
    while(!done) {
      if (tso.tsoData.isNotEmpty()) {
        tso.tsoData.forEach {
          if (it.tsoMessage != null) {
            val tsoMsg = it.tsoMessage
            tsoMsg?.data?.let { data ->
              messages.append(data)
              messages.append("\n")
            }
          } else if (it.tsoPrompt != null) {
            if (messages.toString().contains("IKJ56602I COMMAND SYSTEM RESTARTING DUE TO ERROR")) {
              val IKJ56602I = "IKJ56602I COMMAND SYSTEM RESTARTING DUE TO ERROR"
              val msg = messages.toString()
              val startIndex = msg.indexOf("IKJ56602I")
              messages.delete(startIndex, startIndex + IKJ56602I.length + "\nREADY".length)
            } else if (messages.isNotEmpty() && messages.toString().contains("READY")) {
              done = true
            }
            // TSO PROMPT reached without getting any data, retrying
          }
        }
      }
      if (!done) {
        tso.servletKey?.let { tso = getDataFromTso(it) } ?: throw Exception("servlet key missing")
        tsos.add(tso)
      }
    }
    return CollectedResponses(tsos, messages.toString())
  }

  /**
   * Retrieve tso http request response
   *
   * @param servletKey key of tso address space
   * @return z/OSMF tso response, see [TsoResponse]
   * @throws Exception error executing command
   */
  @Throws(Exception::class)
  private fun getDataFromTso(servletKey: String): TsoResponse {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val tsoApi = buildApi<TsoApi>(url, httpClient)
    val call = tsoApi.receiveMessagesFromTso(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      servletKey = servletKey
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception("Follow up TSO Messages from TSO command cannot be retrieved. " + response?.errorBody()?.string())
    }
    return response?.body() as TsoResponse? ?: throw Exception("No body returned")
  }

  /**
   * API method to send data to already started TSO address space, but will read TSO data until a PROMPT is reached.
   *
   * @param command    to send to the TSO address space.
   * @param servletKey returned from a successful start
   * @return response object, see [SendResponse]
   * @throws Exception error executing command
   */
  @Throws(Exception::class)
  fun sendDataToTSOCollect(servletKey: String, command: String): SendResponse {
    if (servletKey.isEmpty()) {
      throw Exception("servletKey not specified")
    }
    if (command.isEmpty()) {
      throw Exception("command  not specified")
    }

    val putResponse = sendDataToTSOCommon(
      SendTsoParams(servletKey = servletKey, data = command)
    )

    val responses = getAllResponses(putResponse)
    return createResponse(responses)
  }

  /**
   * API method to send data to already started TSO address space
   *
   * @param commandParams object with required parameters, see [SendTsoParams] object
   * @return response object, see [TsoResponse]
   * @throws Exception error executing command
   */
  @Throws(Exception::class)
  fun sendDataToTSOCommon(commandParams: SendTsoParams): TsoResponse {
    if (commandParams.data.isEmpty()) {
      throw Exception("commandParams data not specified")
    }
    if (commandParams.servletKey.isEmpty()) {
      throw Exception("commandParams servletKey not specified")
    }

    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val tsoApi = buildApi<TsoApi>(url, httpClient)
    val call = tsoApi.sendMessageToTso(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      body = TsoData(
        tsoResponse = MessageType(version = "0100", data = commandParams.data)
      ),
      servletKey = commandParams.servletKey
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(
        "No results from executing tso command after getting TSO address space. "
            + response?.errorBody()?.string()
      )
    }
    return response?.body() as TsoResponse? ?: throw Exception("No body returned")
  }

}