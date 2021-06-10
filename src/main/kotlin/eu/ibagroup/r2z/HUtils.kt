// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HRecall(
  @SerializedName("request")
  @Expose
  private val request: String = "hrecall",


  @SerializedName("wait")
  @Expose
  var wait: Boolean? = null
)

data class HMigrate(
  @SerializedName("request")
  @Expose
  private val request: String = "hmigrate",

  @SerializedName("wait")
  @Expose
  var wait: Boolean? = null
)

data class HDelete(
  @SerializedName("request")
  @Expose
  private val request: String = "hdelete",

  @SerializedName("wait")
  @Expose
  var wait: Boolean? = null,

  @SerializedName("purge")
  @Expose
  var purge: Boolean? = null
)