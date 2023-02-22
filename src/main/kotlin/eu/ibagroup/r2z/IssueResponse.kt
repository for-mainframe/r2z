// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * z/OSMF synchronous console command response messages. See the z/OSMF REST API publication for complete details.
 */
data class IssueResponse(

  /**
   * Follow-up response URL.
   */
  @SerializedName("cmd-response-url")
  @Expose
  val cmdResponseUrl: String? = null,

  /**
   * Command response text.
   */
  @SerializedName("cmd-response")
  @Expose
  val cmdResponse: String? = null,

  /**
   * The follow-up response URI.
   */
  @SerializedName("cmd-response-uri")
  @Expose
  val cmdResponseUri: String? = null,

  /**
   * The command response key used for follow-up requests.
   */
  @SerializedName("cmd-response-key")
  @Expose
  val cmdResponseKey: String? = null,

  /**
   * True if the solicited keyword requested is present.
   */
  @SerializedName("sol-key-detected")
  @Expose
  val solKeyDetected: String? = null

)