// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosconsole

import eu.ibagroup.r2z.IssueResponse

/**
 * The Console API response.
 */
data class ConsoleResponse(

  /**
   * True if the command was issued and the responses were collected.
   */
  val success: Boolean? = false,

  /**
   * The list of zOSMF console API responses. May issue multiple requests (because of user request) or
   * to ensure that all messages are collected. Each individual response is placed here.
   */
  val zosmfResponse: IssueResponse? = null,

  /**
   * If an error occurs, returns the ImperativeError, which contains case error.
   */
  val failureResponse: String? = null,

  /**
   * The command response text.
   */
  var commandResponse: String? = null,

  /**
   * The final command response key - used to "follow-up" and check for additional response messages for the command.
   */
  val lastResponseKey: String? = null,

  /**
   * If the solicited keyword is specified, indicates that the keyword was detected.
   */
  val keywordDetected: Boolean? = false,

  /**
   * The "follow-up" command response URL - you can paste this in the browser to do a "GET" using the command
   * response key provided in the URI route.
   */
  val cmdResponseUrl: String? = null

)