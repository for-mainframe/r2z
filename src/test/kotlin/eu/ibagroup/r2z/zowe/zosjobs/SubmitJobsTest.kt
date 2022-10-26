// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosjobs

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import eu.ibagroup.r2z.Intrdr_Recfm
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.SubmitJobs
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.SubmitJclParams
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.SubmitJobParams
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubmitJobsTest {
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
  fun submitJob() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val submitJobs = SubmitJobs(connection, proxyClient)

    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs")) == true &&
            String(it.body).matches(Regex(".*\"file\":\"//'TEST.JOBS\\(JOBNAME\\)'\".*"))
      },
      { MockResponse().setBody(responseDispatcher.readMockJson("submitJobs")) }
    )
    val response = submitJobs.submitJob("//'TEST.JOBS(JOBNAME)'")
    Assertions.assertEquals("ZOSMFAD", response.owner)
    Assertions.assertEquals("JOBNAME", response.jobname)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun submitJobCommon() {
    val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val submitJobs = SubmitJobs(conn, proxyClient)
    val params = SubmitJobParams(
      jobDataSet = "//'TEST.JCL(TESTJOB)'"
    )
    responseDispatcher.injectEndpoint({
      it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs")) == true &&
              String(it.body).matches(Regex(".*\"file\":\"//'TEST.JCL\\(TESTJOB\\)'\".*"))
    }, {
      MockResponse().setBody(responseDispatcher.readMockJson("submitJobCommonResponse"))
    })
    val jobSubmitResponse = submitJobs.submitJobCommon(params)
    Assertions.assertEquals("TESTJOB", jobSubmitResponse.jobname)

    responseDispatcher.clearValidationList()
  }

  @Test
  fun submitJcl() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val submitJobs = SubmitJobs(connection, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs")) == true &&
            it.getHeader("X-IBM-Intrdr-Recfm") == Intrdr_Recfm.F.value &&
            it.getHeader("X-IBM-Intrdr-Lrecl") == "F"
      },
      { MockResponse().setBody(responseDispatcher.readMockJson("submitJobs")).setResponseCode(201) }
    )
    val response = submitJobs.submitJcl(
      javaClass.classLoader.getResource("mock/submitJcl.txt")?.readText() ?: "nothing",
      Intrdr_Recfm.F,
      "F"
    )
    Assertions.assertEquals("JOBNAME", response.jobname)
  }

  @Test
  fun submitJclCommon() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val submitJobs = SubmitJobs(connection, proxyClient)

    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restjobs/jobs")) == true &&
            it.getHeader("X-IBM-Intrdr-Recfm") == Intrdr_Recfm.F.value &&
            it.getHeader("X-IBM-Intrdr-Lrecl") == "F"
      },
      { MockResponse().setBody(responseDispatcher.readMockJson("submitJobs")).setResponseCode(201) }
    )
    val params = SubmitJclParams(
      jcl = javaClass.classLoader.getResource("mock/submitJcl.txt")?.readText() ?: "nothing",
      internalReaderRecfm = Intrdr_Recfm.F,
      internalReaderLrecl = "F"
    )
    val response = submitJobs.submitJclCommon(params)
    Assertions.assertEquals("ZOSMFAD", response.owner)
  }

}