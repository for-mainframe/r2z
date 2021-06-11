/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright © 2021 IBA Group, a.s.
 */

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

