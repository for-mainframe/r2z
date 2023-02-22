// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zostso

import eu.ibagroup.r2z.TsoResponse

/**
 * The TsoStartStop API response
 */
data class StartStopResponse(

  /**
   * Response from z/OSMF to start rest call
   */
  val tsoResponse: TsoResponse? = null,

  /**
   * Servlet key from ZosmfTsoResponse
   */
  val servletKey: String? = null,

  /**
   * If an error occurs, returns error which contains cause error.
   */
  var failureResponse: String? = null,

  /**
   * True if the command was issued and the responses were collected.
   */
  val success: Boolean? = false

)