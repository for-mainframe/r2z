// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input

/**
 * This interface defines the options that can be sent into the copy data set function.
 */
class CopyParams (

  /**
   * The volume to copy from.
   */
  val fromVolser: String ?= null,

  /**
   * The dataset to copy from.
   */
  val fromDataSet: String,

  /**
   * The volume to copy too
   */
  val toVolser: String ?= null,

  /**
   * The dataset to copy too
   */
  val toDataSet: String,

  /**
   * Replace option
   */
  val replace: Boolean = false,

  /**
   * Specified as true to indicate a copying of all members in partial dataset to another partial dataset request
   */
  val copyAllMembers: Boolean = false

)