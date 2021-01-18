package ibagroup.eu.r2z

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class StepData (
  @SerializedName("smfid")
  @Expose
  private var smfId: String? = null,

  @SerializedName("completion")
  @Expose
  private var completion: String? = null,

  @SerializedName("step-number")
  @Expose
  private var stepNumber: Int,

  @SerializedName("program-name")
  @Expose
  private var programName: String,

  @SerializedName("end-time")
  @Expose
  private var endTime: String? = null,

  @SerializedName("active")
  @Expose
  private var isActive: Boolean,

  @SerializedName("step-name")
  @Expose
  private var stepName: String,

  @SerializedName("proc-step-name")
  @Expose
  private var procedureStepName: String,

  @SerializedName("selected-time")
  @Expose
  private var startedTime: String? = null,

  @SerializedName("owner")
  @Expose
  private var owner: String? = null,

  @SerializedName("path-name")
  @Expose
  private var ussPathName: String? = null,
)