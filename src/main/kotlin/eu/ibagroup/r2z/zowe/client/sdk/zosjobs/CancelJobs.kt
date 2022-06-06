// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.ModifyJobParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * CancelJobs class to handle Job cancel
 */
class CancelJobs(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Cancel a job that resides in a z/OS data set.
   *
   * @param jobName name of job to cancel
   * @param jobId   job id
   * @param version version number
   * @return job document with details about the canceled job
   * @throws Exception error canceling
   */
  fun cancelJob(jobName: String, jobId: String, version: RequestVersion): CancelJobRequest {
    return cancelJobCommon(ModifyJobParams(jobName, jobId, version))
  }

  /**
   * Cancel a job that resides in a z/OS data set.
   *
   * @param job     job document wanting to cancel
   * @param version version number
   * @return job document with details about the canceled job
   * @throws Exception error canceling
   */
  fun cancelJobForJob(job: Job, version: RequestVersion): CancelJobRequest {
    return cancelJobCommon(ModifyJobParams(job.jobName, job.jobId, version))
  }

  /**
   * Cancel a job that resides in a z/OS data set.
   *
   * @param params cancel job parameters, see ModifyJobParams object
   * @return job document with details about the canceled job
   * @throws Exception error canceling
   */
  fun cancelJobCommon(params: ModifyJobParams): CancelJobRequest {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApi<JESApi>(url, httpClient)
    val call = jesApi.cancelJobRequest(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      jobName = params.jobName,
      jobId = params.jobId,
      body = CancelJobRequestBody(RequestTypes.CANCEL, params.version!!)
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as CancelJobRequest? ?: throw Exception("No body returned")
  }
}