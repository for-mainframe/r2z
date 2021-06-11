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

data class UssFile(

  @SerializedName("name")
  @Expose
  val name: String = "",

  @SerializedName("mode")
  @Expose
  val mode: String? = null,

  @SerializedName("size")
  @Expose
  val size: Long? = null,

  @SerializedName("uid")
  @Expose
  val uid: Long? = null,

  @SerializedName("user")
  @Expose
  val user: String? = null,

  @SerializedName("gid")
  @Expose
  val gid: Long? = null,

  @SerializedName("group")
  @Expose
  val groupId: String? = null,

  @SerializedName("mtime")
  @Expose
  val modificationTime: String? = null,

  @SerializedName("target")
  @Expose
  val target: String? = null

) {

  val fileMode
    get() = mode?.let { FileMode(it) } ?: FileMode(0, 0, 0)
  val isDirectory
    get() = mode?.first() == 'd'
  val isSymlink
    get() = target != null

}