// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs

import eu.ibagroup.r2z.Job
import eu.ibagroup.r2z.UnsafeOkHttpClient
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.GetJobParams
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.MonitorJobWaitForParams
import okhttp3.OkHttpClient


class MonitorJobs(
  val connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  private val DEFAULT_LINE_LIMIT: Int = 1000
  private val DEFAULT_STATUS = Job.Status.OUTPUT
  private val DEFAULT_WATCH_DELAY: Long = 3000

  /**
   * Default number of poll attempts to check for the specified job status.
   */
  private val DEFAULT_ATTEMPTS = 1000

  companion object {
    data class CheckJobStatus(
      val statusFound: Boolean,
      val job: Job
    )
  }

  /**
   * Given a Job document (has jobname/jobid), waits for the status of the job to be "OUTPUT". This API will poll for
   * the OUTPUT status once every 3 seconds indefinitely. If the polling interval/duration is NOT sufficient, use
   * "waitForStatusCommon" to adjust.
   * <p>
   * See JSDoc for "waitForStatusCommon" for full details on polling and other logic.
   *
   * @param job document of the z/OS job to wait for (see z/OSMF Jobs APIs for details)
   * @return job document
   * @throws Exception error processing wait check request
   */
  @Throws(Exception::class)
  fun waitForJobOutputStatus(job: Job): Job {
    return waitForStatusCommon(
      MonitorJobWaitForParams(job.jobId, job.jobName, Job.Status.OUTPUT, DEFAULT_WATCH_DELAY, DEFAULT_ATTEMPTS)
    )
  }

  /**
   * Given the jobname/jobid, waits for the status of the job to be "OUTPUT". This API will poll for the OUTPUT status
   * once every 3 seconds indefinitely. If the polling interval/duration is NOT sufficient, use
   * "waitForStatusCommon" to adjust.
   *
   *
   * See JavaDoc for "waitForStatusCommon" for full details on polling and other logic.
   *
   * @param jobName the z/OS jobname of the job to wait for output status (see z/OSMF Jobs APIs for details)
   * @param jobId   the z/OS jobid of the job to wait for output status (see z/OSMF Jobs APIS for details)
   * @return job document
   * @throws Exception error processing wait check request
   */
  @Throws(java.lang.Exception::class)
  fun waitForJobOutputStatus(jobName: String, jobId: String): Job {
    return waitForStatusCommon(
      MonitorJobWaitForParams(jobId, jobName, Job.Status.OUTPUT, DEFAULT_WATCH_DELAY, DEFAULT_ATTEMPTS)
    )
  }

  /**
   * Given jobname/jobid, checks for the desired "status" (default is "OUTPUT") continuously (based on the interval
   * and attempts specified).
   *
   *
   * The "order" of natural job status is INPUT ACTIVE OUTPUT. If the requested status is earlier in the sequence
   * than the current status of the job, then the method returns immediately (since the job will never enter the
   * requested status) with the current status of the job.
   *
   * @param params monitor jobs parameters, see MonitorJobWaitForParams object
   * @return job document
   * @throws Exception error processing wait check request
   */
  @Throws(Exception::class)
  fun waitForStatusCommon(params: MonitorJobWaitForParams): Job {
    params.jobName ?: throw IllegalArgumentException("job name not specified")
    params.jobId ?: throw IllegalArgumentException("job id not specified")
    if (params.attempts == null) {
      params.attempts = DEFAULT_ATTEMPTS
    }
    if (params.watchDelay == null) {
      params.watchDelay = DEFAULT_WATCH_DELAY
    }
    return pollForStatus(params)
  }

  /**
   * "Polls" (sets timeouts and continuously checks) for the status of the job to match the desired status.
   *
   * @param params monitor jobs params, see MonitorJobWaitForParams
   * @return job document
   * @throws Exception error processing poll check request
   */
  @Throws(Exception::class)
  private fun pollForStatus(params: MonitorJobWaitForParams): Job {
    val timeoutVal = params.watchDelay ?: DEFAULT_WATCH_DELAY
    var expectedStatus: Boolean // no assigment means by default it is false
    var shouldContinue: Boolean // no assigment means by default it is false
    var numOfAttempts = 0
    val maxAttempts = params.attempts ?: DEFAULT_ATTEMPTS
    var checkJobStatus: CheckJobStatus
    do {
      numOfAttempts++
      checkJobStatus = checkStatus(params)
      expectedStatus = checkJobStatus.statusFound
      shouldContinue = !expectedStatus && maxAttempts > 0 && numOfAttempts < maxAttempts
      if (shouldContinue) {
        Thread.sleep(timeoutVal)
      }
    } while (shouldContinue)
    if (numOfAttempts == maxAttempts) {
      throw Exception("Desired status not seen. The number of maximum attempts reached.")
    }
    return checkJobStatus.job
  }

  /**
   * Checks the status of the job for the expected status (OR that the job has progressed passed the expected status).
   *
   * @param params monitor jobs params, see MonitorJobWaitForParams
   * @return boolean true when the job status is obtained
   * @throws Exception error processing check request
   */
  @Throws(Exception::class)
  private fun checkStatus(params: MonitorJobWaitForParams): CheckJobStatus {
    val getJobs = GetJobs(connection, httpClient)
    val statusCheck = params.jobStatus ?: DEFAULT_STATUS
    val job = getJobs.getStatus(
      params.jobName ?: throw Exception("job name not specified"),
      params.jobId ?: throw Exception("job id not specified")
    )

    if (statusCheck == (job.status ?: DEFAULT_STATUS)) {
      return CheckJobStatus(true, job)
    }
    val orderIndexOfDesiredJobStatus = statusCheck.getOrderIndex()
    val orderIndexOfCurrRunningJobStatus = job.status?.getOrderIndex()  ?: throw Exception("job status not specified")
    return if (orderIndexOfCurrRunningJobStatus > orderIndexOfDesiredJobStatus) {
      CheckJobStatus(true, job)
    } else CheckJobStatus(false, job)
  }

  /**
   * Checks the status order of the given status name
   *
   * @param this status
   * @return int index of status order
   */
  private fun Job.Status.getOrderIndex(): Int {
    return arrayListOf(Job.Status.INPUT, Job.Status.ACTIVE, Job.Status.OUTPUT).indexOf(this).also {
      if (it == -1) {
        throw Exception("Invalid status when checking for status ordering.")
      }
    }
  }

  /**
   * Checks if the given message is within the job output within line limit.
   *
   * @param params monitor jobs params, see MonitorJobWaitForParams
   * @param message message string
   * @return boolean message found status
   * @throws Exception error processing check request
   */
  @Throws(Exception::class)
  private fun checkMessage(params: MonitorJobWaitForParams, message: String): Boolean {
    val getJobs = GetJobs(connection, httpClient)
    val jobs = getJobs.getJobsCommon(GetJobParams(
      prefix = params.jobName ?: throw Exception("job name not specified"),
      jobId = params.jobId ?: throw Exception("job id not specified")
    ))

    if (jobs.isEmpty()) {
      throw Exception("job does not exist")
    }
    val files = getJobs.getSpoolFilesForJob(jobs[0])
    val output = getJobs.getSpoolContent(files[0]).split("\n")
    val lineLimit = params.lineLimit ?: DEFAULT_LINE_LIMIT
    val size = output.size
    val start = if (size < lineLimit) 0 else size - lineLimit

    for (i in start until size) {
      if (output[i].contains(message)) {
        return true
      }
    }
    return false
  }

  /**
   * "Polls" (sets timeouts and continuously checks) for the given message within the job output.
   *
   * @param params monitor jobs params, see MonitorJobWaitForParams
   * @param message message string
   * @return boolean message found status
   * @throws Exception error processing poll check request
   */
  @Throws(Exception::class)
  private fun pollForMessage(params: MonitorJobWaitForParams, message: String): Boolean {
    val timeoutVal = params.watchDelay ?: DEFAULT_WATCH_DELAY
    var messageFound: Boolean // no assigment means by default it is false
    var shouldContinue: Boolean // no assigment means by default it is false
    var numOfAttempts = 0
    val maxAttempts = params.attempts ?: DEFAULT_ATTEMPTS
    do {
      numOfAttempts++
      messageFound = checkMessage(params, message)
      shouldContinue = !messageFound && (maxAttempts > 0 && numOfAttempts < maxAttempts)
      if (shouldContinue) {
        Thread.sleep(timeoutVal)
        if (!isJobRunning(params)) {
          return false
        }
      }
    } while (shouldContinue)
    return numOfAttempts != maxAttempts
  }

  /**
   * Determines if a given job is in a running state or not.
   *
   * @param params monitor jobs params, see MonitorJobWaitForParams
   * @return true if in running state
   * @throws Exception error processing running status check
   */
  @Throws(Exception::class)
  fun isJobRunning(params: MonitorJobWaitForParams): Boolean {
    val getJobs = GetJobs(connection, httpClient)
    val jobName = params.jobName ?: throw Exception("job name not specified")
    val jobId = params.jobId ?: throw Exception("job id not specified")
    val status = getJobs.getStatusValue(jobName, jobId)
    return Job.Status.INPUT.value != status && Job.Status.OUTPUT.value != status
  }

  /**
   * Given jobname/jobid, checks for the desired message continuously (based on the interval and attempts specified).
   *
   * @param params monitor jobs parameters, see MonitorJobWaitForParams object
   * @param message message string
   * @return job document
   * @throws Exception error processing wait check request
   */
  @Throws(Exception::class)
  fun waitForMessageCommon(params: MonitorJobWaitForParams, message: String): Boolean {
    if (params.attempts == null) {
      params.attempts = DEFAULT_ATTEMPTS
    }
    if (params.watchDelay == null) {
      params.watchDelay = DEFAULT_WATCH_DELAY
    }
    if (params.lineLimit == null) {
      params.lineLimit = DEFAULT_LINE_LIMIT
    }
    return pollForMessage(params, message)
  }

  /**
   * Given a Job document (has jobname/jobid), waits for the given message from the job. This API will poll for
   * the given message once every 3 seconds for at least 1000 times. If the polling interval/duration is NOT
   * sufficient, use "waitForMessageCommon" method to adjust.
   *
   * @param job document of the z/OS job to wait for (see z/OSMF Jobs APIs for details)
   * @param message message string
   * @return job document
   * @throws Exception error processing wait check request
   */
  @Throws(Exception::class)
  fun waitForJobMessage(job: Job, message: String): Boolean {
    return waitForMessageCommon(MonitorJobWaitForParams(
      jobName = job.jobName,
      jobId = job.jobId,
      jobStatus = job.status,
      watchDelay = DEFAULT_WATCH_DELAY,
      attempts = DEFAULT_ATTEMPTS,
      lineLimit = DEFAULT_LINE_LIMIT
    ), message)
  }

  /**
   * Given a Job document (has jobname/jobid), waits for the given status of the job. This API will poll for
   * the given status once every 3 seconds for at least 1000 times. If the polling interval/duration is NOT
   * sufficient, use "waitForStatusCommon" method to adjust.
   * <p>
   * See JavaDoc for "waitForStatusCommon" for full details on polling and other logic.
   *
   * @param job        document of the z/OS job to wait for (see z/OSMF Jobs APIs for details)
   * @param statusType status type, see JobStatus.Type object
   * @return job document
   * @throws Exception error processing wait check request
   */
  @Throws(Exception::class)
  fun waitForJobStatus(job: Job, statusType: Job.Status): Job {
    return waitForStatusCommon(
      MonitorJobWaitForParams(job.jobId, job.jobName, statusType, DEFAULT_WATCH_DELAY, DEFAULT_ATTEMPTS)
    )
  }

  /**
   * Given the jobname/jobid, waits for the given status of the job. This API will poll for the given status once
   * every 3 seconds for at least 1000 times. If the polling interval/duration is NOT sufficient, use
   * "waitForStatusCommon" method to adjust.
   *
   * @param jobName the z/OS jobname of the job to wait for output status (see z/OSMF Jobs APIs for details)
   * @param jobId the z/OS jobid of the job to wait for output status (see z/OSMF Jobs APIS for details)
   * @param statusType status type, see JobStatus.Type object
   * @return job document
   * @throws Exception error processing wait check request
   */
  @Throws(Exception::class)
  fun waitForJobStatus(jobName: String, jobId: String, statusType: Job.Status): Job {
    return waitForStatusCommon(MonitorJobWaitForParams(
      jobName = jobName,
      jobId = jobId,
      jobStatus = statusType,
      attempts = DEFAULT_ATTEMPTS,
      watchDelay = DEFAULT_WATCH_DELAY
    ))
  }

}