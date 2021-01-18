package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RenameData(
  @SerializedName("request")
  @Expose
  private val request: String = "rename",

  @SerializedName("from-dataset")
  @Expose
  private val fromDataset: FromDataset,

  @SerializedName("enq")
  @Expose
  private val enq: Enq? = null


) {
  enum class Enq(private var type: String) {

    @SerializedName("EXCLU")
    EXCLU("EXCLU"),
    @SerializedName("SHRW")
    SHRW("SHRW");

    override fun toString(): String {
      return type
    }


  }

  data class FromDataset(

    @SerializedName("dsn")
    @Expose
    private val oldDatasetName: String,

    @SerializedName("member")
    @Expose
    private val oldMemberName: String? = null,

    )
}

