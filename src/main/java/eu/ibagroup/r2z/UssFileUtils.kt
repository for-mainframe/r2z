package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class ChangeMode(
  @SerializedName("request")
  @Expose
  private val request: String = "chmod",
  @SerializedName("mode")
  @Expose
  @JsonAdapter(ToStringAdapter::class)
  private val mode: FileMode,

  @SerializedName("links")
  private val links: Links? = null,

  @SerializedName("recursive")
  private val recursive: Boolean? = null

)

data class ChangeOwner(
  @SerializedName("request")
  @Expose
  private val request: String = "chown",
  @SerializedName("owner")
  @Expose
  private val owner: String,

  @SerializedName("group")
  @Expose
  private val group: String?,

  @SerializedName("links")
  private val links: Links? = null,

  @SerializedName("recursive")
  private val recursive: Boolean? = null

)

data class ChangeTag(
  @SerializedName("request")
  @Expose
  private val request: String = "chtag",
  @SerializedName("action")
  @Expose
  private val action: TagAction,

  @SerializedName("type")
  @Expose
  private val type: XIBMDataType?,

  @SerializedName("links")
  private val links: Links? = null,

  @SerializedName("codeset")
  private val codeSet: Links? = null,

  @SerializedName("recursive")
  private val recursive: Boolean? = null

)

enum class TagAction {
  @SerializedName("set")
  SET,
  @SerializedName("remove")
  REMOVE,
  @SerializedName("list")
  LIST
}

enum class Links {
  @SerializedName("follow")
  FOLLOW,
  @SerializedName("suppress")
  SUPPRESS,
  @SerializedName("change")
  CHANGE
}

