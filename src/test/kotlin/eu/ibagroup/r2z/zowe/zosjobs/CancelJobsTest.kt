// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosjobs

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.CancelJobs
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CancelJobsTest {
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
  fun cancelJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val cancelJobs = CancelJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/TESTJOB2/JOB00084")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("cancelJobs")).setResponseCode(200)
      }
    )
    val response = cancelJobs.cancelJob("TESTJOB2", "JOB00084", RequestVersion.SYNCHRONOUS)
    Assertions.assertEquals("0", response.status)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun cancelJobForJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val cancelJobs = CancelJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs/TESTJOB2/JOB00084")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("cancelJobs")).setResponseCode(200)
      }
    )
    val response = cancelJobs.cancelJobForJob(
      Job(
        jobId = "JOB00084", jobName = "TESTJOB2", owner = "", type = Job.JobType.JOB,
        url = "", filesUrl = "", phase = 0, phaseName = ""
      ),
      RequestVersion.SYNCHRONOUS
    )
    Assertions.assertEquals("0", response.status)
  }
}