// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

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