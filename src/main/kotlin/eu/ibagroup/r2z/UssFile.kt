// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

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