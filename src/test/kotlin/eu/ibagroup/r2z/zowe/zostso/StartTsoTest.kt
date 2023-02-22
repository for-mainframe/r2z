// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zostso

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zostso.StartTso
import eu.ibagroup.r2z.zowe.client.sdk.zostso.input.StartTsoParams
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StartTsoTest {
  lateinit var mockServer: MockWebServer
  lateinit var proxyClient: OkHttpClient
  lateinit var responseDispatcher: MockResponseDispatcher

  @BeforeAll
  fun createMockServer() {
    mockServer = MockWebServer()
    responseDispatcher = MockResponseDispatcher()
    mockServer.dispatcher = responseDispatcher
    mockServer.start()
    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(mockServer.hostName, mockServer.port))
    proxyClient = OkHttpClient.Builder().proxy(proxy).build()
  }

  @AfterAll
  fun stopMockServer() {
    mockServer.shutdown()
  }

  @Test
  fun start() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val startTso = StartTso(connection, proxyClient)

    responseDispatcher.injectEndpoint(
      {
        it?.requestLine?.matches(Regex("POST http://.*/zosmf/tsoApp/tso.*proc=DBSPROCB.*acct=IZUACCT.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("startTso") ?: "")
      }
    )
    responseDispatcher.injectEndpoint(
      {
        it?.requestLine?.matches(Regex("GET http://.*/zosmf/tsoApp/tso/.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("receiveMessagesFromTso") ?: "")
      }
    )

    val startTsoParams = StartTsoParams(
      characterSet = "697",
      codePage = "1047",
      columns = "160",
      rows = "204",
      logonProcedure = "DBSPROCB",
      regionSize = "50000"
    )
    val response = startTso.start("IZUACCT", startTsoParams)

    Assertions.assertTrue(response.success)
    Assertions.assertEquals(null, response.failureResponse)
    Assertions.assertEquals("DLIS-121-aabcaaat", response.tsoResponse?.servletKey)

    responseDispatcher.clearValidationList()
  }

}
