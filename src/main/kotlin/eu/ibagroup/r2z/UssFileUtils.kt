// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

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
  var mode: FileMode,

  @SerializedName("links")
  var links: Links? = null,

  @SerializedName("recursive")
  var recursive: Boolean? = null

)

data class ChangeOwner(
  @SerializedName("request")
  @Expose
  private val request: String = "chown",
  @SerializedName("owner")
  @Expose
  var owner: String,

  @SerializedName("group")
  @Expose
  var group: String?,

  @SerializedName("links")
  var links: Links? = null,

  @SerializedName("recursive")
  var recursive: Boolean? = null

)

data class ChangeTag(
  @SerializedName("request")
  @Expose
  private val request: String = "chtag",
  @SerializedName("action")
  @Expose
  var action: TagAction,

  @SerializedName("type")
  @Expose
  var type: XIBMDataType?,

  @SerializedName("links")
  var links: Links? = null,

  @SerializedName("codeset")
  var codeSet: Links? = null,

  @SerializedName("recursive")
  var recursive: Boolean? = null

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

