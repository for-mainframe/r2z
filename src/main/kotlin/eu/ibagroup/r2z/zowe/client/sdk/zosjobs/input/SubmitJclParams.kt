// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input

import eu.ibagroup.r2z.Intrdr_Recfm

/**
 * Submit jcl parameters
 */
class SubmitJclParams(

  /**
   * JCL to submit which should contain syntactically correct JCL
   */
  val jcl: String,

  /**
   * Specify internal reader RECFM and corresponding http(s) headers will be appended to the request accordingly
   * "F" (fixed) or "V" (variable)
   */
  val internalReaderRecfm: Intrdr_Recfm,

  /**
   * Specify internal reader LRECL and corresponding http(s) headers will be appended to the request accordingly
   * "F" (fixed) or "V" (variable)
   */
  val internalReaderLrecl: String,

  /**
   * A string for JCL symbolic substitution
   */
  val jclSymbols: String? = null
)