// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SpoolFile(

  @SerializedName("jobname")
  @Expose
  val jobname: String,

  @SerializedName("recfm")
  @Expose
  val recfm: String,

  @SerializedName("byte-count")
  @Expose
  val byteCount: Int,

  @SerializedName("record-count")
  @Expose
  val recordCount: Int,

  @SerializedName("job-correlator")
  @Expose
  val jobCorrelator: String? = null,

  @SerializedName("class")
  @Expose
  val fileClass: String,

  @SerializedName("jobid")
  @Expose
  val jobId: String,

  @SerializedName("id")
  @Expose
  val id: Int,

  @SerializedName("ddname")
  @Expose
  val ddName: String,

  @SerializedName("records-url")
  @Expose
  val recordsUrl: String,

  @SerializedName("lrecl")
  @Expose
  val recordLength: Int,

  @SerializedName("subsystem")
  @Expose
  val subsystem: String? = null,

  @SerializedName("stepname")
  @Expose
  val stepName: String? = null,

  @SerializedName("procstep")
  @Expose
  val procStep: String? = null
)