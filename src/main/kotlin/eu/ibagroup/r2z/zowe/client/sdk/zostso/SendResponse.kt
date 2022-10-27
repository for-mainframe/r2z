// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zostso

import eu.ibagroup.r2z.TsoResponse

/**
 * The TsoSend API response
 */
data class SendResponse(

  /**
   * True if the command was issued and the responses were collected.
   */
  val success: Boolean,

  /**
   * The list of zOSMF send API responses. May issue multiple requests or
   * to ensure that all messages are collected. Each individual response is placed here.
   */
  val tsoResponses: List<TsoResponse> = emptyList(),

  /**
   * The command response text.
   */
  val commandResponse: String? = null
)
