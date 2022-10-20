// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zostso

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zostso.input.StartTsoParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * Start TSO address space and receive servlet key
 */
class StartTso(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Start TSO address space with provided parameters.
   *
   * @param accountNumber this key of StartTsoParams required, because it cannot be default.
   * @param params        optional object with required parameters, see [StartTsoParams]
   * @return command response on resolve, see [StartStopResponses]
   * @throws Exception error executing command
   */
  @Throws(Exception::class)
  fun start(accountNumber: String, params: StartTsoParams): StartStopResponses {
    if (accountNumber.isEmpty()) {
      throw Exception("accountNumber not specified")
    }

    val customParams = StartTsoParams(
      account = accountNumber,
      characterSet = params.characterSet ?: TsoConstants.DEFAULT_CHSET,
      codePage = params.codePage ?: TsoConstants.DEFAULT_CPAGE,
      rows = params.rows ?: TsoConstants.DEFAULT_ROWS,
      columns = params.columns ?: TsoConstants.DEFAULT_COLS,
      logonProcedure = params.logonProcedure ?: TsoConstants.DEFAULT_PROC,
      regionSize = params.regionSize ?: TsoConstants.DEFAULT_RSIZE,
    )

    val tsoResponse = startCommon(customParams)

    var collectedResponses: CollectedResponses? = null
    if (tsoResponse.servletKey != null) {
      val sendTso = SendTso(connection, httpClient)
      collectedResponses = sendTso.getAllResponses(tsoResponse)
    }

    return StartStopResponses(tsoResponse, collectedResponses)
  }

  /**
   * Start TSO address space with provided parameters
   *
   * @param commandParams object with required parameters, see [StartTsoParams]
   * @return z/OSMF response object, see [TsoResponse]
   * @throws Exception error executing command
   */
  @Throws(Exception::class)
  fun startCommon(commandParams: StartTsoParams): TsoResponse {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val tsoApi = buildApi<TsoApi>(url, httpClient)
    val call = tsoApi.startTso(
      authorizationToken = Credentials.basic(connection.user, connection.password),
      acct = commandParams.account ?: throw Exception("account num not specified"),
      proc = commandParams.logonProcedure ?: TsoConstants.DEFAULT_PROC,
      chset = commandParams.characterSet ?: TsoConstants.DEFAULT_CHSET,
      cpage = commandParams.codePage ?: TsoConstants.DEFAULT_CPAGE,
      rows = commandParams.rows?.toInt() ?: TsoConstants.DEFAULT_ROWS.toInt(),
      cols = commandParams.columns?.toInt() ?: TsoConstants.DEFAULT_COLS.toInt(),
      rsize = commandParams.regionSize?.toInt() ?: TsoConstants.DEFAULT_RSIZE.toInt()
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception("No results from executing tso command while setting up TSO address space. "
          + response?.errorBody()?.string())
    }
    return response?.body() as TsoResponse? ?: throw Exception("No body returned")
  }
}