// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input

import eu.ibagroup.r2z.RequestVersion

/**
 * ModifyJobParams APIs parameters interface for delete and cancel job operations
 */
class ModifyJobParams (

  /**
   * Job name value specified for request
   */
  val jobName: String,

  /**
   * Job id value specified for request
   */
  val jobId: String,

  /**
   * Version value specified for request
   */
  val version: RequestVersion? = null
)