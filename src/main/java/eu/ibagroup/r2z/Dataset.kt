package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Dataset(
  @SerializedName("dsname")
  @Expose
  val name: String = "",

  @SerializedName("blksz")
  @Expose
  val blockSize: Int? = null,

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
  val extendsUsed: Int? = null,

  @SerializedName("lrecl")
  @Expose
  val recordLength: Int? = null,

  @SerializedName("migr")
  @Expose
  val migrated:  HasMigrated? = null,

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
  val spaceUnits: String? = null,

  @SerializedName("used")
  @Expose
  val usedTracksOrPages: String? = null,

  @SerializedName("vol")
  @Expose
  val volumeSerial: String? = null,

  @SerializedName("vols")
  @Expose
  val volumeSerials: String? = null

)

interface  HasBooleanValue {
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