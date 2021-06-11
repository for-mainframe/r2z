/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright © 2021 IBA Group, a.s.
 */

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