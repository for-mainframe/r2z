package eu.ibagroup.r2z

import com.google.gson.annotations.SerializedName

data class MoveUssFile(
  @SerializedName("request")
  private val request: String = "move",

  @SerializedName("from")
  private val from: String,

  @SerializedName("overwrite")
  private val overwrite: Boolean? = null
)