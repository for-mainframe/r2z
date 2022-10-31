// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * The z/OSMF console API parameters. See the z/OSMF REST API documentation for full details.
 */
class IssueRequestBody(

  /**
   * The z/OS console command to issue.
   */
  @SerializedName("cmd")
  @Expose
  val cmd: String,

  /**
   * The solicited keyword to look for.
   */
  @SerializedName("sol-key")
  @Expose
  val solKey: String? = null,

  /**
   * The system in the sysplex to route the command.
   */
  @SerializedName("system")
  @Expose
  val system: String? = null,

  /**
   * The method of issuing the command.
   */
  @SerializedName("async")
  @Expose
  val async: String? = null

)