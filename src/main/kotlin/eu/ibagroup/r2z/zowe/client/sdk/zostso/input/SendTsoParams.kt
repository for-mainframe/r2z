// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zostso.input

/**
 * TSO issue command z/OSMF parameters
 */
data class SendTsoParams(

  /**
   * Servlet key of an active address space.
   */
  val servletKey: String,

  /**
   * Data to be sent to the active address space.
   */
  val data: String
)