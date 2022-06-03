// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input

import eu.ibagroup.r2z.XIBMAttr

/**
 * This interface defines the options that can be sent into the list data set function
 */
class ListParams(

  /**
   * The volume where the data set resides
   */
  val volume: String? = null,

  /**
   * The indicator that specifies the attribute type
   */
  val attribute: XIBMAttr.Type = XIBMAttr.Type.BASE,

  /**
   * The indicator that specifies the maximum number of items to return
   */
  val maxLength: Int? = null,

  /**
   * An optional search parameter that specifies the first data set name to return to the response document
   */
  val start: String? = null,

  /**
   * An optional parameter that specifies how to handle migrated data sets
   */
  val recall: String? = null,

  /**
   * An optional pattern for restricting the response list
   */
  val pattern: String? = null,

  /**
   * Response time out value
   */
  val responseTimeout: String? = null,

  /**
   * A boolean parameter that shows total rows property of the attribute
   */
  val returnTotalRows: Boolean = true

)
