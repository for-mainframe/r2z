// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zostso

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zostso.IssueTso
import eu.ibagroup.r2z.zowe.client.sdk.zostso.input.StartTsoParams
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IssueTsoTest {
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
  fun issueTsoTest() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val issueTso = IssueTso(connection, proxyClient)

    val startTsoParams = StartTsoParams(
      characterSet = "697",
      codePage = "1047",
      columns = "160",
      rows = "204",
      logonProcedure = "DBSPROCB",
      regionSize = "50000"
    )

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
        it?.requestLine?.matches(Regex("PUT http://.*/zosmf/tsoApp/tso/.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("sendMessageToTso") ?: "")
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
    responseDispatcher.injectEndpoint(
      {
        it?.requestLine?.matches(Regex("DELETE http://.*/zosmf/tsoApp/tso/DLIS-121-aabcaaat HTTP/.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("stopTso") ?: "")
      }
    )

    val response = issueTso.issueTsoCommand("IZUACCT", "TIME", startTsoParams)

    Assertions.assertTrue(response.success)
    Assertions.assertEquals("DLIS-121-aabcaaat", response.startResponse?.servletKey)
    Assertions.assertTrue(
      response.commandResponses?.contains(
        "IKJ56650I TIME-06:43:59 PM. CPU-00:00:00 SERVICE-595 SESSION-00:05:00 SEPTEMBER 22,2022") == true
    )
    Assertions.assertEquals("DLIS-121-aabcaaat", response.stopResponse?.servletKey)

    responseDispatcher.clearValidationList()
  }

}
