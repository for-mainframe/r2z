// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zostso

import eu.ibagroup.r2z.TsoResponse

/**
 * The TsoStartStop API responses
 */
class StartStopResponses(

  /**
   * Response from z/OSMF to start rest call
   */
  val tsoResponse: TsoResponse?,

  collectedResponses: CollectedResponses?
) {

  /**
   * If an error occurs, returns the error which contains cause error.
   */
  val failureResponse: String?

  /**
   * Appended collected messages including READY prompt at the end.
   */
  val messages: String

  /**
   * True if the command was issued and the responses were collected.
   */
  val success: Boolean

  /**
   * Collected responses from z/OSMF
   */
  var collectedResponses: List<TsoResponse>

  /**
   * Servlet key from TsoResponse
   */
  var servletKey: String

  init {
    tsoResponse ?: throw Exception("tsoResponse is null")

    if (tsoResponse.msgData.isNotEmpty()) {
      this.success = false
      val zosmfMsg = tsoResponse.msgData[0]
      this.failureResponse = zosmfMsg.messageText ?: "zOSMF unknown error response"
    } else {
      this.success = true
      this.failureResponse = null
    }

    this.servletKey = tsoResponse.servletKey ?: throw Exception("servletKey is missing")

    val tsoMsgLst = tsoResponse.tsoData
    this.messages = tsoMsgLst.joinToString()
    this.collectedResponses = collectedResponses?.tsos ?: emptyList()
  }
}
