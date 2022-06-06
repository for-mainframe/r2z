// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosjobs

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import eu.ibagroup.r2z.Job
import eu.ibagroup.r2z.RequestVersion
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.DeleteJobs
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteJobsTest {

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

  @Test
  fun deleteJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val deleteJobs = DeleteJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/TESTJOBW/JOB00085")) == true &&
            it.getHeader("X-IBM-Job-Modify-Version") == RequestVersion.SYNCHRONOUS.value
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("deleteJobs")).setResponseCode(200)
      }
    )
    val response = deleteJobs.deleteJob("TESTJOBW", "JOB00085", RequestVersion.SYNCHRONOUS)
    Assertions.assertEquals("0", response.status)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun deleteJobForJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val deleteJobs = DeleteJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/TESTJOBW/JOB00085")) == true &&
            it.getHeader("X-IBM-Job-Modify-Version") == RequestVersion.SYNCHRONOUS.value
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("deleteJobs")).setResponseCode(200)
      }
    )
    val response = deleteJobs.deleteJobForJob(
      Job(
        jobId = "JOB00085", jobName = "TESTJOBW", owner = "", type = Job.JobType.JOB,
        url = "", filesUrl = "", phase = 0, phaseName = ""
      ),
      RequestVersion.SYNCHRONOUS
    )
    Assertions.assertEquals("0", response.status)
  }
}