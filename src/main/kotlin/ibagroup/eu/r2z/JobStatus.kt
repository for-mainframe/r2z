package ibagroup.eu.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class JobStatus(
  @SerializedName("owner")
  @Expose
  var owner: String,

  @SerializedName("phase")
  @Expose
  var phase: Int,

  @SerializedName("subsystem")
  @Expose
  var subSystem: String? = null,

  @SerializedName("phase-name")
  @Expose
  var phaseName: String,

  @SerializedName("job-correlator")
  @Expose
  var jobCorrelator: String? = null,

  @SerializedName("type")
  @Expose
  var type: JobType,

  @SerializedName("url")
  @Expose
  var url: String,

  @SerializedName("jobid")
  @Expose
  var jobId: String,

  @SerializedName("class")
  @Expose
  var jobClass: String? = null,

  @SerializedName("files-url")
  @Expose
  var filesUrl: String,

  @SerializedName("jobname")
  @Expose
  var jobName: String,

  @SerializedName("status")
  @Expose
  var status: Status? = null,

  @SerializedName("retcode")
  @Expose
  var returnedCode: String? = null,

  @SerializedName("step-data")
  @Expose
  var steps: List<StepData> = emptyList(),

  @SerializedName("reason-not-running")
  @Expose
  var reasonNotRunning: String? = null
)

enum class Status (val value: String) {
  @SerializedName("INPUT")
  INPUT("INPUT"),
  @SerializedName("ACTIVE")
  ACTIVE("ACTIVE"),
  @SerializedName("OUTPUT")
  OUTPUT("OUTPUT");

  override fun toString(): String {
    return value
  }
}

enum class JobType(val value: String){
  @SerializedName("JOB")
  JOB("JOB"),
  @SerializedName("STC")
  STC("STC"),
  @SerializedName("TSU")
  TSU("TSU");

  override fun toString(): String {
    return value
  }
}