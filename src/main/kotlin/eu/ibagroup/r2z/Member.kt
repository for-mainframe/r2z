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
import com.google.gson.annotations.SerializedName

data class Member(
  @SerializedName("member")
  @Expose
  val name: String = "",

  @SerializedName("vers")
  @Expose
  val versionNumber: Int? = null,

  @SerializedName("mod")
  @Expose
  val modificationLevel: Int? = null,

  @SerializedName("c4date")
  @Expose
  val creationDate: String? = null,

  @SerializedName("m4date")
  @Expose
  val modificationDate: String? = null,

  @SerializedName("cnorc")
  @Expose
  val currentNumberOfRecords: Int? = null,

  @SerializedName("inorc")
  @Expose
  val beginningNumberOfRecords: Int? = null,

  @SerializedName("mnorc")
  @Expose
  val numberOfChangedRecords: Int? = null,

  @SerializedName("mtime")
  @Expose
  val lastChangeTime: String? = null,

  @SerializedName("msec")
  @Expose
  val secondsOfLastChangeTime: String? = null,

  @SerializedName("user")
  @Expose
  val user: String? = null,

  @SerializedName("sclm")
  @Expose
  val sclm: String? = null,

  @SerializedName("ac")
  @Expose
  val authorizationCode: String? = null,

  @SerializedName("alias-of")
  @Expose
  val aliasOf: String? = null,

  @SerializedName("amode")
  @Expose
  val amode: String? = null,

  @SerializedName("attr")
  @Expose
  val loadModuleAttributes: String? = null,

  @SerializedName("rmode")
  @Expose
  val rmode: String? = null,

  @SerializedName("size")
  @Expose
  val size: String? = null,

  @SerializedName("ttr")
  @Expose
  val ttr: String? = null,

  @SerializedName("ssi")
  @Expose
  val ssi: String? = null
)