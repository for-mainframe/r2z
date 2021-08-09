// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class CreateUssFile(

  @SerializedName("type")
  @Expose
  var type: FileType,

  @SerializedName("mode")
  @Expose
  @JsonAdapter(ToStringAdapter::class)
  var mode: FileMode
)

enum class FileModeValue(val mode: Int) {
  NONE(0),
  EXECUTE(1),
  WRITE(2),
  WRITE_EXECUTE(3),
  READ(4),
  READ_EXECUTE(5),
  READ_WRITE(6),
  READ_WRITE_EXECUTE(7)
}

class FileMode(
  var owner: Int,
  var group: Int = 0,
  var all: Int = 0,
  var prefix: String = ""
) {

  constructor(modeString: CharSequence, prefix: String = "") : this(
    stringToDigit(modeString.subSequence(1, 4)),
    stringToDigit(modeString.subSequence(4, 7)),
    stringToDigit(modeString.subSequence(7, 10)),
    prefix
  )

  constructor (
    owner: FileModeValue,
    group: FileModeValue = FileModeValue.NONE,
    all: FileModeValue = FileModeValue.NONE,
    prefix: String = ""
  ) : this(owner = owner.mode, group = group.mode, all = all.mode, prefix)


  override fun toString(): String {
    return prefix + digitToString(owner) + digitToString(group) + digitToString(all)
  }

}

interface ToStringSerializable

enum class FileType(private val type: String) {
  @SerializedName("dir")
  DIR("dir"),

  @SerializedName("file")
  FILE("file");

  override fun toString(): String {
    return type
  }


}

fun stringToDigit(modeString: CharSequence): Int {
  return when (modeString) {
    "---" -> 0
    "--x" -> 1
    "-w-" -> 2
    "-wx" -> 3
    "r--" -> 4
    "r-x" -> 5
    "rw-" -> 6
    "rwx" -> 7
    else -> 0
  }
}

fun digitToString(digit: Int): String {
  return when (digit) {
    0 -> "---"
    1 -> "--x"
    2 -> "-w-"
    3 -> "-wx"
    4 -> "r--"
    5 -> "r-x"
    6 -> "rw-"
    7 -> "rwx"
    else -> "---"
  }
}

fun Int.toFileModeValue() : FileModeValue {
  return when (this) {
    0 -> FileModeValue.NONE
    1 -> FileModeValue.EXECUTE
    2 -> FileModeValue.WRITE
    3 -> FileModeValue.WRITE_EXECUTE
    4 -> FileModeValue.READ
    5 -> FileModeValue.READ_EXECUTE
    6 -> FileModeValue.READ_WRITE
    7 -> FileModeValue.READ_WRITE_EXECUTE
    else -> FileModeValue.NONE
  }
}



