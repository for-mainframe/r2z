package eu.ibagroup.r2z

import com.google.gson.annotations.SerializedName

data class MoveUssFile(
  @SerializedName("request")
  var request: String = "move",

  @SerializedName("from")
  var from: String,

  @SerializedName("overwrite")
  var overwrite: Boolean? = null
)