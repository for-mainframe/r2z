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

data class ReleaseJobRequest(
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
    var status: Int? = null
)