// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input

/**
 * Interface for various GetJobs APIs
 */
class GetJobParams (

  /**
   * Owner for which to obtain jobs for.
   */
  val owner: String? = null,

  /**
   * Prefix to filter when obtaining jobs.
   */
  val prefix: String? = null,

  /**
   * Max jobs to return in a list
   */
  val maxJobs: Int? = null,

  /**
   * job id for a job
   */
  val jobId: String? = null

)