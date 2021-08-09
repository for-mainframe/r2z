// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import eu.ibagroup.r2z.annotations.AvailableSince
import eu.ibagroup.r2z.annotations.ZVersion

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
  var allocationUnit : AllocationUnit? = null,

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

  @SerializedName("storclass")
  @Expose
  var storageClass: String? = null,

  @SerializedName("mgntclass")
  @Expose
  var managementClass: String? = null,

  @SerializedName("dataclass")
  @Expose
  var dataClass: String? = null,

  @SerializedName("avgblk")
  var averageBlockLength: Int? = null,

  @SerializedName("dsntype")
  @Expose
  var dsnType: DsnameType? = null,

  @AvailableSince(ZVersion.ZOS_2_4)
  @SerializedName("like")
  @Expose
  var datasetModel: String? = null
)

enum class AllocationUnit(private val type : String) {
  @SerializedName("TRK")
  TRK("TRK"),
  @SerializedName("CYL")
  CYL("CYL"),
  @Deprecated("This one doesn't work in z/OSMF. Just to simplify For Mainframe plugin")
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
