// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.ModifyJobParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * DeleteJobs class to handle Job delete
 */
class DeleteJobs(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Cancel and purge job from spool.
   *
   * @param jobName name of job to delete
   * @param jobId   job id
   * @param version version number
   * @return job document with details about the deleted job
   * @throws Exception error deleting
   */
  fun deleteJob(jobName: String, jobId: String, version: RequestVersion): CancelJobPurgeOutRequest {
    return deleteJobCommon(ModifyJobParams(jobName, jobId, version))
  }

  /**
   * Cancel and purge job from spool.
   *
   * @param job     job document wanting to delete
   * @param version version number
   * @return job document with details about the deleted job
   * @throws Exception error deleting
   */
  fun deleteJobForJob(job: Job, version: RequestVersion): CancelJobPurgeOutRequest {
    return deleteJobCommon(ModifyJobParams(job.jobName, job.jobId, version))
  }

  /**
   * Delete a job that resides in a z/OS data set.
   *
   * @param params delete job parameters, see ModifyJobParams object
   * @return job document with details about the deleted job
   * @throws Exception error on deleting
   */
  fun deleteJobCommon(params: ModifyJobParams): CancelJobPurgeOutRequest {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApi<JESApi>(url, httpClient)
    val call = jesApi.cancelJobPurgeOutRequest(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      jobName = params.jobName,
      jobId = params.jobId,
      version = params.version.let {
        if (it?.value == ProcessMethod.SYNCHRONOUS.value) ProcessMethod.SYNCHRONOUS else ProcessMethod.ASYNCHRONOUS
      }
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as CancelJobPurgeOutRequest? ?: throw Exception("No body returned")
  }
}