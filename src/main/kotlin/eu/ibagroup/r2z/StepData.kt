// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class StepData (
  @SerializedName("smfid")
  @Expose
  val smfId: String? = null,

  @SerializedName("completion")
  @Expose
  val completion: String? = null,

  @SerializedName("step-number")
  @Expose
  val stepNumber: Int,

  @SerializedName("program-name")
  @Expose
  val programName: String,

  @SerializedName("end-time")
  @Expose
  val endTime: String? = null,

  @SerializedName("active")
  @Expose
  val isActive: Boolean,

  @SerializedName("step-name")
  @Expose
  val stepName: String,

  @SerializedName("proc-step-name")
  @Expose
  val procedureStepName: String,

  @SerializedName("selected-time")
  @Expose
  val startedTime: String? = null,

  @SerializedName("owner")
  @Expose
  val owner: String? = null,

  @SerializedName("path-name")
  @Expose
  val ussPathName: String? = null,
)