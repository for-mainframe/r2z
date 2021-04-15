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