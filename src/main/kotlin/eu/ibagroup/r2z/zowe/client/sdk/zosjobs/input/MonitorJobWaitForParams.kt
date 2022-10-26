// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input

import eu.ibagroup.r2z.Job

data class MonitorJobWaitForParams(
    val jobId: String? = null,
    val jobName: String? = null,
    val jobStatus: Job.Status? = null,
    var watchDelay: Long? = null,
    var attempts: Int? = null,
    var lineLimit: Int? = null
)