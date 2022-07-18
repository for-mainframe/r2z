// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosjobs

import com.google.gson.Gson
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import eu.ibagroup.r2z.Job
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.MonitorJobs
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.MonitorJobWaitForParams
import okhttp3.OkHttpClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MonitorJobsTest {
  lateinit var mockServer: MockWebServer
  lateinit var proxyClient: OkHttpClient
  lateinit var responseDispatcher: MockResponseDispatcher

  @BeforeAll
  fun createMockServer() {
    mockServer = MockWebServer()
    responseDispatcher = MockResponseDispatcher()
    mockServer.setDispatcher(responseDispatcher)
    thread(start = true) {
      mockServer.play()
    }
    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(mockServer.hostName, mockServer.port))
    proxyClient = OkHttpClient.Builder().proxy(proxy).build()
  }

  @AfterAll
  fun stopMockServer() {
    mockServer.shutdown()
  }

  private fun Job.cloneWithChangedStatus(status: Job.Status): Job {
    return Job(jobId, jobName, subSystem, owner, status, type, jobClass, returnedCode, url, filesUrl, jobCorrelator, phase, phaseName)
  }

  @Test
  fun waitForJobStatus () {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val monitorJobs = MonitorJobs(connection, proxyClient)
    val mockJobString = responseDispatcher.readMockJson("getJob") ?: throw Exception("File \"getJob.json\" is not found in mock data.")
    val mockJobStringPrepared = mockJobString.substring(1, mockJobString.length - 1)
    val job = Gson().fromJson(mockJobStringPrepared, Job::class.java)
    val startTime = System.currentTimeMillis()
    var requestTimes = 0
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/${job.jobName}/${job.jobId}.*")) == true
      },
      {
        ++requestTimes
        val jobRsp = if (System.currentTimeMillis() - startTime < 6000) job.cloneWithChangedStatus(Job.Status.ACTIVE)
        else job
        MockResponse().setBody(Gson().toJson(jobRsp)).setResponseCode(200)
      }
    )
    monitorJobs.waitForJobOutputStatus(job.jobName, job.jobId)
    Assertions.assertEquals(3, requestTimes)
  }

  @Test
  fun waitForJobMessage() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val monitorJobs = MonitorJobs(connection, proxyClient)
    val mockJobString = responseDispatcher.readMockJson("jobForTestingRetrieveSpoolContent") ?: throw Exception("File \"1\" is not found in mock data.")
    val mockJobStringPrepared = mockJobString.substring(1, mockJobString.length - 1)
    val job = Gson().fromJson(mockJobStringPrepared, Job::class.java)
    val mockSpoolFiles = responseDispatcher.readMockJson("getSpoolFilesForTestingRetrieveSpoolContent") ?: throw Exception("File \"2\" is not found in mock data.")
    val mockSpoolFileContent = javaClass.classLoader.getResource("mock/getJcl.txt")?.readText()
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs\\?prefix=IJMP05&jobid=JOB09502")) == true
      },
      {
        MockResponse().setBody(mockJobString).setResponseCode(200)
      }
    )
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/${job.jobName}/${job.jobId}/files.*")) == true
      },
      {
        MockResponse().setBody(mockSpoolFiles).setResponseCode(200)
      }
    )
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/${job.jobName}/${job.jobId}/files/2/records")) == true
      },
      {
        MockResponse().setBody(mockSpoolFileContent).setResponseCode(200)
      }
    )
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/${job.jobName}/${job.jobId}.*")) == true
      },
      {
        MockResponse().setBody(Gson().toJson(job)).setResponseCode(200)
      }
    )
    val doesMessageExist = monitorJobs.waitForJobMessage(job, "JOB09502")
    Assertions.assertEquals(true, doesMessageExist)

    responseDispatcher.clearValidationList()
  }

}