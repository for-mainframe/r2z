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
  val size: Int? = null,

  @SerializedName("uid")
  @Expose
  val uid: Int? = null,

  @SerializedName("user")
  @Expose
  val user: String? = null,

  @SerializedName("gid")
  @Expose
  val gid: Int? = null,

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

  val fileMode = mode?.let { FileMode(it) } ?: FileMode(0, 0, 0)
  val isDirectory = mode?.first() == 'd'
  val isSymlink = target != null

}