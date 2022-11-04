// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zostso

import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import eu.ibagroup.r2z.TsoResponse
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zostso.SendTso
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SendTsoTest {
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
  fun getAllResponses() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val sendTso = SendTso(connection, proxyClient)

    responseDispatcher.injectEndpoint(
      {
        it?.requestLine?.matches(Regex("GET http://.*/zosmf/tsoApp/tso/.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("receiveMessagesFromTso") ?: "")
      }
    )

    val tsoResponse = Gson().fromJson(responseDispatcher.readMockJson("startTso"), TsoResponse::class.java)
    val response = sendTso.getAllResponses(tsoResponse)

    Assertions.assertEquals(tsoResponse.servletKey, response.tsos[0].servletKey)
    Assertions.assertTrue(response.messages?.contains("LOGON IN PROGRESS AT 17:31:11 ON SEPTEMBER 22, 2022") == true)
  }

  @Test
  fun sendDataToTSOCollect() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val sendTso = SendTso(connection, proxyClient)

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

    val servletKey = "DLIS-121-aabcaaat"
    val response = sendTso.sendDataToTSOCollect(servletKey, "TIME")

    Assertions.assertEquals(servletKey, response.tsoResponses[0].servletKey)
    Assertions.assertTrue(
      response.commandResponse?.contains(
        "IKJ56650I TIME-06:43:59 PM. CPU-00:00:00 SERVICE-595 SESSION-00:05:00 SEPTEMBER 22,2022") == true
    )

    responseDispatcher.clearValidationList()
  }
}
