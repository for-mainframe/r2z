// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitJobRequest (
    @SerializedName("jobid")
    @Expose
    var jobid: String? = null,

    @SerializedName("jobname")
    @Expose
    var jobname: String? = null,

    @SerializedName("subsystem")
    @Expose
    var subsystem: String? = null,

    @SerializedName("owner")
    @Expose
    var owner: String? = null,

    @SerializedName("status")
    @Expose
    var status: Status? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("class")
    @Expose
    var class_: String? = null,

    @SerializedName("retcode")
    @Expose
    var retcode: String? = null,

    @SerializedName("url")
    @Expose
    var url: String? = null,

    @SerializedName("files-url")
    @Expose
    var filesUrl: String? = null
)

{
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