// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosfiles

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.ZosDsnDownload
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input.DownloadParams
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZosDsnDownloadTest {
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
  fun testDownloadDsn() {
    val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val zosDsnDownload = ZosDsnDownload(conn, proxyClient)
    val downloadParams = DownloadParams(
    )
    val responseBody = javaClass.classLoader.getResource("mock/downloadDsnMember.txt")?.readText()
    responseDispatcher.injectEndpoint(
      { it?.path?.matches(Regex("http://.*/zosmf/restfiles/ds/TEST.JCL\\(TESTJOB\\)")) == true &&
        it.method?.equals("GET") == true
      },
      { MockResponse().setBody(responseBody) }
    )
    val stream = zosDsnDownload.downloadDsn("TEST.JCL(TESTJOB)", downloadParams)
    val content = stream.bufferedReader().use { it.readText() }
    Assertions.assertEquals(responseBody, content)

    responseDispatcher.clearValidationList()
  }

}