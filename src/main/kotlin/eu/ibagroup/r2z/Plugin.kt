// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Plugin(
  @SerializedName("pluginVersion")
  @Expose
  val version: String? = null,

  @SerializedName("pluginDefaultName")
  @Expose
  val defaultName: String? = null,

  @SerializedName("pluginStatus")
  @Expose
  val status: String? = null
)