// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.CommonJobParams
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.GetJobParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * Class to handle obtaining of z/OS batch job information
 */
class GetJobs(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Get jobs (defaults to the user ID of the session as owner).
   *
   * @return list of job objects (matching jobs)
   * @throws Exception error on getting a list of jobs
   */
  fun getJobs(): List<Job> {
    return getJobsCommon(GetJobParams())
  }

  /**
   * Get jobs that match a job name by prefix. Defaults to job(s) owned by the user ID in the session.
   *
   * @param prefix job name prefix for which to list jobs. Supports wildcard e.g. JOBNM*
   * @return list of job objects (matching jobs)
   * @throws Exception error on getting a list of jobs
   */
  fun getJobsByPrefix(prefix: String): List<Job> {
    if (prefix.isEmpty()) {
      throw Exception("Prefix not specified")
    }
    return getJobsCommon(GetJobParams(prefix = prefix))
  }

  /**
   * Get jobs that are owned by a certain user or pattern of users.
   *
   * @param owner owner for which to get jobs. Supports wildcard e.g.
   *              IBMU* returns jobs owned by all users whose ID beings with "IBMU"
   * @return list of job objects (matching jobs)
   * @throws Exception error on getting a list of jobs
   */
  fun getJobsByOwner(owner: String): List<Job> {
    if (owner.isEmpty()) {
      throw Exception("Owner not specified")
    }
    return getJobsCommon(GetJobParams(owner = owner))
  }

  /**
   * Get a list of jobs that match an owner and prefix.
   *
   * @param owner  owner for which to get jobs. Supports wildcard e.g.
   *               IBMU* returns jobs owned by all users whose ID beings with "IBMU"
   * @param prefix prefix for which to get jobs. Supports wildcard e.g.
   *               JOBNM* returns jobs with names starting with "JOBNM"
   * @return list of job objects (matching jobs)
   * @throws Exception error on getting a list of jobs
   */
  fun getJobsByOwnerAndPrefix(owner: String, prefix: String): List<Job> {
    if (prefix.isEmpty()) {
      throw Exception("Prefix not specified")
    }
    if (owner.isEmpty()) {
      throw Exception("Owner not specified")
    }
    return getJobsCommon(GetJobParams(prefix = prefix, owner = owner))
  }

  /**
   * Get a single job object from an input job id.
   *
   * @param jobId job ID for the job for which you want to get status
   * @return job object (matching jobs)
   * @throws Exception error on getting job
   */
  fun getJob(jobId: String): Job {
    val jobs = getJobsCommon(GetJobParams(jobId = jobId))

    if (jobs.isEmpty()) {
      throw Exception("Job not found")
    }
    if (jobs.size > 1) {
      throw Exception("Expected 1 job returned but received ${jobs.size} jobs")
    }
    return jobs[0]
  }

  /**
   * Get jobs filtered by owner and prefix.
   *
   * @param params get job parameters, see GetJobParams object
   * @return list of job objects (matching jobs)
   * @throws Exception error on getting a list of jobs
   */
  @Suppress("UNCHECKED_CAST")
  fun getJobsCommon(params: GetJobParams): List<Job> {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApi<JESApi>(url, httpClient)

    val call = jesApi.getFilteredJobs(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      owner = (
          if (params.owner == null && params.prefix == null && params.jobId == null && params.maxJobs == null) {
            connection.user
          } else {
            params.owner
          }),
      prefix = params.prefix,
      jobId = params.jobId,
      maxCount = params.maxJobs
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as List<Job>? ?: throw Exception("No body returned")
  }

  /**
   * Get the status value only for a given job name and id.
   *
   * @param jobName job name for the job for which you want to get status
   * @param jobId   job ID for the job for which you want to get status
   * @return status value
   * @throws Exception error getting job status
   */
  fun getStatusValue(jobName: String, jobId: String): String {
    val job = getStatusCommon(CommonJobParams(jobName, jobId))
    return job.status?.value ?: throw Exception("Job status is missing")
  }

  /**
   * Get the status value for a given job object.
   *
   * @param job job document
   * @return status value
   * @throws Exception error getting job status
   */
  fun getStatusValueForJob(job: Job): String {
    val result = getStatusCommon(CommonJobParams(job.jobName, job.jobId))
    return result.status?.value ?: throw Exception("Job status is missing")
  }

  /**
   * Get the status and other details (e.g. owner, return code) for a job.
   *
   * @param jobName job name for the job for which you want to get status
   * @param jobId   job ID for the job for which you want to get status
   * @return job document (matching job)
   * @throws Exception error getting job status
   */
  fun getStatus(jobName: String, jobId: String): Job {
    return getStatusCommon(CommonJobParams(jobName, jobId))
  }

  /**
   * Get the status and other details (e.g. owner, return code) for a job
   * Alternate version of the API that accepts a Job object returned by
   * other APIs such as SubmitJobs. Even though the parameter and return
   * value are of the same type, the Job object returned will have the
   * current status of the job.
   *
   * @param job job document
   * @return job document (matching job)
   * @throws Exception error getting job status
   */
  fun getStatusForJob(job: Job): Job {
    return getStatusCommon(CommonJobParams(job.jobName, job.jobId))
  }

  /**
   * Get the status and other details (e.g. owner, return code) for a job.
   *
   * @param params common job parameters, see CommonJobParams object
   * @return job document (matching job)
   * @throws Exception error getting job status
   */
  fun getStatusCommon(params: CommonJobParams): Job {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApi<JESApi>(url, httpClient)
    val call = jesApi.getJob(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      jobName = params.jobName,
      jobId = params.jobId
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as Job? ?: throw Exception("No body returned")
  }

  /**
   * Get a list of all spool files for a job.
   *
   * @param jobName job name for the job for which you want to get a list of spool files
   * @param jobId   job ID for the job for which you want to get a list of spool files
   * @return list of JobFile objects
   * @throws Exception error on getting spool files info
   */
  fun getSpoolFiles(jobName: String, jobId: String): List<SpoolFile> {
    return getSpoolFilesCommon(CommonJobParams(jobName, jobId))
  }

  /**
   * Get a list of all job spool files for a job.
   *
   * @param params common job parameters, see CommonJobParams object
   * @return list of SpoolFile objects
   * @throws Exception error on getting spool files info
   */
  @Suppress("UNCHECKED_CAST")
  fun getSpoolFilesCommon(params: CommonJobParams): List<SpoolFile> {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApi<JESApi>(url, httpClient)
    val call = jesApi.getJobSpoolFiles(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      jobId = params.jobId,
      jobName = params.jobName
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as List<SpoolFile>? ?: throw Exception("No body returned")
  }

  /**
   * Get a list of all job spool files for a job.
   * Alternate version of the API that accepts a Job object returned by
   * other APIs such as SubmitJobs.
   *
   * @param job job for which you would like to get a list of job spool files
   * @return list of SpoolFile objects
   * @throws Exception error on getting spool files info
   */
  fun getSpoolFilesForJob(job: Job): List<SpoolFile> {
    if (job.jobName.isEmpty()) {
      throw Exception("job name not specified")
    }
    if (job.jobId.isEmpty()) {
      throw Exception("job id not specified")
    }
    return getSpoolFilesCommon(CommonJobParams(jobId = job.jobId, jobName = job.jobName))
  }

  /**
   * Get the JCL that was used to submit a job.
   *
   * @param params common job parameters, see CommonJobParams object
   * @return JCL content
   * @throws Exception error on getting jcl content
   */
  fun getJclCommon(params: CommonJobParams): String {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApiWithBytesConverter<JESApi>(url, httpClient)
    val call = jesApi.getJCLRecords(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      jobName = params.jobName,
      jobId = params.jobId
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return String(response?.body() as ByteArray? ?: throw Exception("No body returned"))
  }

  /**
   * Get JCL from a job.
   *
   * @param jobName job name for the job for which you want to retrieve JCL
   * @param jobId   job ID for the job for which you want to retrieve JCL
   * @return job document on resolve
   * @throws Exception error on getting jcl content
   */
  fun getJcl(jobName: String, jobId: String): String {
    return getJclCommon(CommonJobParams(jobName = jobName, jobId = jobId))
  }

  /**
   * Get JCL from a job.
   * Alternate version of the API that accepts a Job object returned by
   * other APIs such as SubmitJobs.
   *
   * @param job job for which you would like to retrieve JCL
   * @return JCL content
   * @throws Exception error on getting jcl content
   */
  fun getJclForJob(job: Job): String {
    return getJclCommon(CommonJobParams(jobId = job.jobId, jobName = job.jobName))
  }

  /**
   * Get spool content from a job (keeping naming convention patter with this duplication function).
   *
   * @param file spool file for which you want to retrieve the content
   * @return spool content
   * @throws Exception error on getting spool content
   */
  fun getSpoolContent(file: SpoolFile): String {
    return getSpoolContentCommon(file)
  }

  /**
   * Get spool content from a job using the job name, job ID, and spool ID number from z/OSMF.
   *
   * @param jobName job name for the job containing the spool content
   * @param jobId   job id for the job containing the spool content
   * @param spoolId id number assigned by zosmf that identifies the particular job spool file (DD)
   * @return spool content
   * @throws Exception error on getting spool content
   */
  fun getSpoolContentById(jobName: String, jobId: String, spoolId: Int): String {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApiWithBytesConverter<JESApi>(url, httpClient)
    val call = jesApi.getSpoolFileRecords(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      jobName = jobName,
      jobId = jobId,
      fileId = spoolId
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return String(response?.body() as ByteArray? ?: throw Exception("No body returned"))
  }

  /**
   * Get spool content from a job.
   *
   * @param file spool file for which you want to retrieve the content
   * @return spool content
   * @throws Exception error on getting spool content
   */
  fun getSpoolContentCommon(file: SpoolFile): String {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApiWithBytesConverter<JESApi>(url, httpClient)
    val call = jesApi.getSpoolFileRecords(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      jobName = file.jobname,
      jobId = file.jobId,
      fileId = file.id
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return String(response?.body() as ByteArray? ?: throw Exception("No body returned"))
  }
}