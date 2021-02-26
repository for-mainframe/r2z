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