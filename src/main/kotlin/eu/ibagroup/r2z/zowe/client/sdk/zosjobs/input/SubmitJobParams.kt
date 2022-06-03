// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input

/**
 * Submit job parameters
 */
class SubmitJobParams(

  /**
   * z/OS data set which should contain syntactically correct JCL
   */
  val jobDataSet: String,

  /**
   * A string for JCL symbolic substitution
   */
  val jclSymbols: String? = null

)