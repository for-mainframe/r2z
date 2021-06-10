// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Dataset(
  @SerializedName("dsname")
  @Expose
  val name: String = "",

  @SerializedName("blksz")
  @Expose
  private val blksz: String? = null,

  @SerializedName("catnm")
  @Expose
  val catalogName: String? = null,

  @SerializedName("cdate")
  @Expose
  val creationDate: String? = null,

  @SerializedName("dev")
  @Expose
  val deviceType: String? = null,

  @SerializedName("dsntp")
  @Expose
  val dsnameType: DsnameType? = null,

  @SerializedName("dsorg")
  @Expose
  val datasetOrganization: DatasetOrganization? = null,

  @SerializedName("edate")
  @Expose
  val expirationDate: String? = null,

  @SerializedName("extx")
  @Expose
  private val extx: String? = null,

  @SerializedName("lrecl")
  @Expose
  private val lrecl: String? = null,

  @SerializedName("migr")
  @Expose
  val migrated: HasMigrated? = null,

  @SerializedName("mvol")
  @Expose
  val multiVolume: MultipleVolumes? = null,

  @SerializedName("ovf")
  @Expose
  val spaceOverflowIndicator: String? = null,

  @SerializedName("rdate")
  @Expose
  val lastReferenceDate: String? = null,

  @SerializedName("recfm")
  @Expose
  val recordFormat: RecordFormat? = null,

  @SerializedName("sizex")
  @Expose
  val sizeInTracks: Int? = null,

  @SerializedName("spacu")
  @Expose
  val spaceUnits: SpaceUnits? = null,

  @SerializedName("used")
  @Expose
  private val used: String? = null,

  @SerializedName("vol")
  @Expose
  val volumeSerial: String? = null,

  @SerializedName("vols")
  @Expose
  val volumeSerials: String? = null

) {

  val blockSize: Int?
    get() = if (blksz != null && !blksz.isQuestion()) Integer.parseInt(blksz) else null

  val extendsUsed: Int?
    get() = if (extx != null && !extx.isQuestion()) Integer.parseInt(extx) else null

  val recordLength: Int?
    get() = if (lrecl != null && !lrecl.isQuestion()) Integer.parseInt(lrecl) else null

  val usedTracksOrBlocks: Int?
    get() = if (used != null && !used.isQuestion()) Integer.parseInt(used) else null

  private fun String?.isQuestion() : Boolean {
    return this == "?"
  }

}

enum class SpaceUnits {
  @SerializedName("TRACKS")
  TRACKS,

  @SerializedName("BLOCKS")
  BLOCKS,

  @SerializedName("CYLINDERS")
  CYLINDERS
}

interface HasBooleanValue {
  val value: Boolean
}

enum class MultipleVolumes(override val value: Boolean) : HasBooleanValue {
  @SerializedName("Y")
  Y(true),

  @SerializedName("N")
  N(false)
}


enum class HasMigrated(override val value: Boolean) : HasBooleanValue {
  @SerializedName("YES")
  YES(true),

  @SerializedName("NO")
  NO(false)
}