package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

val ALL_MEMBERS = "*"

class CopyDataZOS {
  data class CopyFromFile(
    @SerializedName("request")
    @Expose
    protected val request: String = "copy",

    @SerializedName("from-file")
    @Expose
    private val file: File,

    @SerializedName("enq")
    @Expose
    private val enq: Enq? = null,

    @SerializedName("replace")
    private val replace: Boolean? = null
  ) {

    enum class Enq(private var type: String) {

      EXCLU("EXCLU"),
      SHRW("SHRW"),
      SHR("SHR");

      override fun toString(): String {
        return type
      }


    }

    data class File(

      @SerializedName("filename")
      @Expose
      private val fileName: String,

      @SerializedName("type")
      @Expose
      private val type: CopyType? = null,

      ) {

      enum class CopyType(private val type: String) {
        @SerializedName("binary")
        BINARY("binary"),

        @SerializedName("executable")
        EXECUTABLE("executable"),

        @SerializedName("text")
        TEXT("text")
      }
    }

  }

  data class CopyFromDataset(
    @SerializedName("request")
    @Expose
    protected val request: String = "copy",

    @SerializedName("from-dataset")
    @Expose
    private val dataset: Dataset,

    @SerializedName("enq")
    @Expose
    private val enq: Enq? = null,

    @SerializedName("replace")
    private val replace: Boolean? = null
  ) {

    enum class Enq(private var type: String) {

      @SerializedName("EXCLU")
      EXCLU("EXCLU"),

      @SerializedName("SHRW")
      SHRW("SHRW"),

      @SerializedName("SHR")
      SHR("SHR");

      override fun toString(): String {
        return type
      }


    }

    data class Dataset(

      @SerializedName("dsn")
      @Expose
      private val datasetName: String,

      @SerializedName("member")
      @Expose
      private val memberName: String? = null,

      @SerializedName("volser")
      @Expose
      private val volumeSerial: String? = null,

      @SerializedName("alias")
      @Expose
      private val alias: Boolean? = null,

      )

  }
}

class CopyDataUSS {

  data class CopyFromFileOrDir(
    @SerializedName("request")
    private val request: String = "copy",

    @SerializedName("from")
    private val from: String,

    @SerializedName("overwrite")
    private val overwrite: Boolean? = null,

    @SerializedName("recursive")
    private val recursive: Boolean? = null,

    @SerializedName("links")
    private val links: Links? = null,

    @SerializedName("preserve")
    private val preserve: Preserve? = null
  )

  data class CopyFromDataset(
    @SerializedName("request")
    private val request: String = "copy",

    @SerializedName("from-dataset")
    private val from: Dataset,

    @SerializedName("overwrite")
    private val overwrite: Boolean? = null,

    @SerializedName("recursive")
    private val recursive: Boolean? = null,

    @SerializedName("links")
    private val links: Links? = null,

    @SerializedName("preserve")
    private val preserve: Preserve? = null
  ) {
    data class Dataset(

      @SerializedName("dsn")
      @Expose
      private val datasetName: String,

      @SerializedName("member")
      @Expose
      private val memberName: String? = null,

      @SerializedName("type")
      @Expose
      private val type: XIBMDataType? = null,

      ) {
      enum class CopyType(private val type: String) {
        @SerializedName("binary")
        BINARY("binary"),

        @SerializedName("executable")
        EXECUTABLE("executable"),

        @SerializedName("text")
        TEXT("text")
      }
    }

  }

  enum class Preserve {
    @SerializedName("none")
    NONE,

    @SerializedName("modtime")
    MODTIME,

    @SerializedName("all")
    ALL
  }

  enum class Links {
    @SerializedName("NONE")
    NONE,

    @SerializedName("SRC")
    SRC,

    @SerializedName("ALL")
    ALL
  }

}