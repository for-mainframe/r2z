package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateDataset(
  @SerializedName("volser")
  @Expose
  private val volumeSerial: String? = null,

  @SerializedName("unit")
  @Expose
  private val deviceType: String? = null,

  @SerializedName("dsorg")
  @Expose
  private val datasetOrganization: DatasetOrganization,

  @SerializedName("alcunit")
  @Expose
  private val allocationUnit : AllocationUnit,

  @SerializedName("primary")
  private val primaryAllocation: Int,

  @SerializedName("secondary")
  @Expose
  private val secondaryAllocation: Int,

  @SerializedName("dirblk")
  @Expose
  private val directoryBlocks : Int? = null,

  @SerializedName("recfm")
  @Expose
  private val recordFormat: RecordFormat,

  @SerializedName("blksize")
  @Expose
  private val blockSize: Int? = null,

  @SerializedName("lrecl")
  @Expose
  private val recordLength: Int? = null,

  @SerializedName("storeclass")
  @Expose
  private val storageClass: String? = null,

  @SerializedName("mgntclass")
  @Expose
  private val managementClass: String? = null,

  @SerializedName("dataclass")
  @Expose
  private val dataClass: String? = null

)

enum class AllocationUnit(private val type : String) {
  @SerializedName("TRK")
  TRK("TRK"),
  @SerializedName("CYL")
  CYL("CYL"),
  @SerializedName("BLK")
  BLK("BLK");

  override fun toString(): String {
    return type
  }

}

enum class DatasetOrganization(private val type: String) {
  @SerializedName("PO")
  PO("PO"),
  @SerializedName("PO-E")
  POE("PO-E"),
  @SerializedName("PS")
  PS("PS"),
  @SerializedName("VS")
  VS("VS");


  override fun toString(): String {
    return type
  }


}

enum class DsnameType {
  @SerializedName("LIBRARY")
  LIBRARY,
  @SerializedName("HFS")
  HFS,
  @SerializedName("PDS")
  PDS,
  @SerializedName("LARGE")
  LARGE,
  @SerializedName("BASIC")
  BASIC,
  @SerializedName("EXTREQ")
  EXTREQ,
  @SerializedName("EXTREF")
  EXTPREF
}

enum class RecordFormat(private val type: String) {
  @SerializedName("F")
  F("F"),
  @SerializedName("FB")
  FB("FB"),
  @SerializedName("V")
  V("V"),
  @SerializedName("VB")
  VB("VB"),
  @SerializedName("U")
  U("U"),
  @SerializedName("VA")
  VA("VA");


  override fun toString(): String {
    return type
  }


}
