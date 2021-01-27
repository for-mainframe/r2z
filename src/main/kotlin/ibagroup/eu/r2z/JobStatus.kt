package ibagroup.eu.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class JobStatus(
  @SerializedName("owner")
  @Expose
  val owner: String,

  @SerializedName("phase")
  @Expose
  val phase: Int,

  @SerializedName("subsystem")
  @Expose
  val subSystem: String? = null,

  @SerializedName("phase-name")
  @Expose
  val phaseName: String,

  @SerializedName("job-correlator")
  @Expose
  val jobCorrelator: String? = null,

  @SerializedName("type")
  @Expose
  val type: JobType,

  @SerializedName("url")
  @Expose
  val url: String,

  @SerializedName("jobid")
  @Expose
  val jobId: String,

  @SerializedName("class")
  @Expose
  val jobClass: String? = null,

  @SerializedName("files-url")
  @Expose
  val filesUrl: String,

  @SerializedName("jobname")
  @Expose
  val jobName: String,

  @SerializedName("status")
  @Expose
  val status: Status? = null,

  @SerializedName("retcode")
  @Expose
  val returnedCode: String? = null,

  @SerializedName("step-data")
  @Expose
  val steps: List<StepData> = emptyList(),

  @SerializedName("reason-not-running")
  @Expose
  val reasonNotRunning: String? = null
) {

  enum class Status(val value: String) {
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

  enum class JobType(val value: String) {
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
}