package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateDataset(
  @SerializedName("volser")
  @Expose
  var volumeSerial: String? = null,

  @SerializedName("unit")
  @Expose
  var deviceType: String? = null,

  @SerializedName("dsorg")
  @Expose
  var datasetOrganization: DatasetOrganization,

  @SerializedName("alcunit")
  @Expose
  var allocationUnit : AllocationUnit,

  @SerializedName("primary")
  var primaryAllocation: Int,

  @SerializedName("secondary")
  @Expose
  var secondaryAllocation: Int,

  @SerializedName("dirblk")
  @Expose
  var directoryBlocks : Int? = null,

  @SerializedName("recfm")
  @Expose
  var recordFormat: RecordFormat,

  @SerializedName("blksize")
  @Expose
  var blockSize: Int? = null,

  @SerializedName("lrecl")
  @Expose
  var recordLength: Int? = null,

  @SerializedName("storeclass")
  @Expose
  var storageClass: String? = null,

  @SerializedName("mgntclass")
  @Expose
  var managementClass: String? = null,

  @SerializedName("dataclass")
  @Expose
  var dataClass: String? = null

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
  @SerializedName("?")
  VSAM("VSAM"),
  @SerializedName("VA")
  VA("VA");



  override fun toString(): String {
    return type
  }


}
