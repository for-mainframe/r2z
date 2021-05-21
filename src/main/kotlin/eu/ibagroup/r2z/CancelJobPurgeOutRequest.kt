package ibagroup.eu.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CancelJobPurgeOutRequest(
    @SerializedName("jobid")
    @Expose
    var jobid: String? = null,

    @SerializedName("jobname")
    @Expose
    var jobname: String? = null,

    @SerializedName("original-jobid")
    @Expose
    var originalJobid: String? = null,

    @SerializedName("owner")
    @Expose
    var owner: String? = null,

    @SerializedName("member")
    @Expose
    var member: String? = null,

    @SerializedName("sysname")
    @Expose
    var sysname: String? = null,

    @SerializedName("job-correlator")
    @Expose
    var jobCorrelator: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null
)