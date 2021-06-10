// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

val ALL_MEMBERS = "*"

class CopyDataZOS {
  data class CopyFromFile(
    @SerializedName("request")
    @Expose
    var request: String = "copy",

    @SerializedName("from-file")
    @Expose
    var file: File,

    @SerializedName("enq")
    @Expose
    var enq: Enq? = null,

    @SerializedName("replace")
    var replace: Boolean? = null
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
      var fileName: String,

      @SerializedName("type")
      @Expose
      var type: CopyType? = null,

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
    private val request: String = "copy",

    @SerializedName("from-dataset")
    @Expose
    var dataset: Dataset,

    @SerializedName("enq")
    @Expose
    var enq: Enq? = null,

    @SerializedName("replace")
    var replace: Boolean? = null
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
      var datasetName: String,

      @SerializedName("member")
      @Expose
      var memberName: String? = null,

      @SerializedName("volser")
      @Expose
      var volumeSerial: String? = null,

      @SerializedName("alias")
      @Expose
      var alias: Boolean? = null,

      )

  }
}

class CopyDataUSS {

  data class CopyFromFileOrDir(
    @SerializedName("request")
    private val request: String = "copy",

    @SerializedName("from")
    var from: String,

    @SerializedName("overwrite")
    var overwrite: Boolean? = null,

    @SerializedName("recursive")
    var recursive: Boolean? = null,

    @SerializedName("links")
    var links: Links? = null,

    @SerializedName("preserve")
    var preserve: Preserve? = null
  )

  data class CopyFromDataset(
    @SerializedName("request")
    private val request: String = "copy",

    @SerializedName("from-dataset")
    var from: Dataset,

    @SerializedName("overwrite")
    var overwrite: Boolean? = null,

    @SerializedName("recursive")
    var recursive: Boolean? = null,

    @SerializedName("links")
    var links: Links? = null,

    @SerializedName("preserve")
    var preserve: Preserve? = null
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