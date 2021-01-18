package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class CreateUssFile(

  @SerializedName("type")
  @Expose
  private val type: FileType,

  @SerializedName("mode")
  @Expose
  @JsonAdapter(ToStringAdapter::class)
  private val mode: FileMode
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
  val owner: Int,
  val group: Int = 0,
  val all: Int = 0,
  val prefix: String = ""
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

