// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosfiles

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.ZosDsnCopy
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input.CopyParams
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZosDsnCopyTest {
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
  fun testCopyDsn() {
    val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val zosDsnCopy = ZosDsnCopy(conn, proxyClient)
    val copyParams = CopyParams(
      fromDataSet = "TEST.JCL(TESTJOB)",
      toDataSet = "NBEL.TEST.DATA",
      replace = true
    )
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restfiles/ds/NBEL.TEST.DATA")) == true && it.method?.equals("PUT") == true
      },
      {
        MockResponse().setResponseCode(200)
      }
    )
    val response = zosDsnCopy.copy(copyParams)
    Assertions.assertEquals(200, response.code())

    responseDispatcher.clearValidationList()
  }

  @Test
  fun testCopy() {
    val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val zosDsnCopy = ZosDsnCopy(conn, proxyClient)
    responseDispatcher.injectEndpoint(
      {
        it?.path?.matches(Regex("http://.*/zosmf/restfiles/ds/NBEL.TEST.DATA")) == true && it.method?.equals("PUT") == true
      }, {
        MockResponse().setResponseCode(200)
      }
    )
    val response = zosDsnCopy.copy("TEST.JCL(TESTJOB)", "NBEL.TEST.DATA", replace = true, copyAllMembers = false)
    Assertions.assertEquals(200, response.code())

    responseDispatcher.clearValidationList()
  }

}