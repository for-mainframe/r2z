// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosconsole

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import eu.ibagroup.r2z.IssueRequestBody
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosconsole.IssueCommand
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IssueCommandTest {
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
  fun issueCommonTest() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val issueCommand = IssueCommand(connection, proxyClient)

    val issueParams = IssueRequestBody(
      cmd = "d a,PEGASUS",
      solKey = "PEGASUS"
    )

    responseDispatcher.injectEndpoint(
      {
        it?.requestLine?.matches(Regex("PUT http://.*/zosmf/restconsoles/consoles/ibmusecn HTTP/.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("issueCommand") ?: "")
      }
    )

    val response = issueCommand.issueCommon("ibmusecn", issueParams)

    Assertions.assertEquals("C8529621", response.cmdResponseKey)
    Assertions.assertTrue(
      response.cmdResponse?.contains(
        " D A,PEGASUS\r\n CNZ4106I 17.06.33 DISPLAY ACTIVITY 128\r") == true
    )

    responseDispatcher.clearValidationList()
  }

  @Test
  fun issueSimple() {
    val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val issueCommand = IssueCommand(connection, proxyClient)

    responseDispatcher.injectEndpoint(
      {
        it?.requestLine?.matches(Regex("PUT http://.*/zosmf/restconsoles/consoles/defcn HTTP/.*")) == true
      },
      {
        MockResponse().setBody(responseDispatcher.readMockJson("issueCommand") ?: "")
      }
    )

    val response = issueCommand.issueSimple("d a,PEGASUS")

    Assertions.assertTrue(response.success == true)
    Assertions.assertEquals("C8529621", response.lastResponseKey)

    responseDispatcher.clearValidationList()
  }



}
