// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zostso.input

/**
 * TSO start command z/OSMF parameters
 */
data class StartTsoParams(

  /**
   * User's z/OS permission account number
   */
  val account: String? = null,

  /**
   * Character set for address space
   */
  val characterSet: String? = null,

  /**
   * Code page for tso address space
   */
  val codePage: String? = null,

  /**
   * Number of columns
   */
  val columns: String? = null,

  /**
   * Name of the logonProcedure for address space
   */
  val logonProcedure: String? = null,

  /**
   * Region size for tso address space
   */
  val regionSize: String? = null,

  /**
   * Number of rows
   */
  val rows: String? = null
)
