package ibagroup.eu.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SpoolFile(
  @SerializedName("recfm")
  @Expose
  val recfm: String,

  @SerializedName("records-url")
  @Expose
  val recordsUrl: String,

  @SerializedName("stepname")
  @Expose
  val stepName: String? = null,

  @SerializedName("subsystem")
  @Expose
  val subsystem: String? = null,

  @SerializedName("job-correlator")
  @Expose
  val jobCorrelator: String? = null,

  @SerializedName("byte-count")
  @Expose
  val byteCount: Int,

  @SerializedName("lrecl")
  @Expose
  val recordLength: Int,

  @SerializedName("jobid")
  @Expose
  val jobId: String,

  @SerializedName("ddname")
  @Expose
  val ddName: String,

  @SerializedName("id")
  @Expose
  val id: Int,

  @SerializedName("record-count")
  @Expose
  val recordCount: Int,

  @SerializedName("class")
  @Expose
  val fileClass: String,

  @SerializedName("jobname")
  @Expose
  val jobname: String,

  @SerializedName("procstep")
  @Expose
  val procStep: String? = null
)