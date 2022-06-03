// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosfiles

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.ZosDsnList
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input.ListParams
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZosDsnListTest {
  lateinit var mockServer: MockWebServer
  lateinit var proxyClient: OkHttpClient
  lateinit var responseDispatcher: MockResponseDispatcher

  @BeforeAll
  fun createMockServer () {
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
  fun stopMockServer () {
    mockServer.shutdown()
  }

  @Test
  fun testListDsn () {
    val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val listParams = ListParams(
      volume = "TESTVOL"
    )
    val zosDsnList = ZosDsnList(conn, proxyClient)
    responseDispatcher.injectEndpoint({
      it?.path?.matches(Regex("http://.*/zosmf/restfiles/ds.*")) == true
    }, {
      MockResponse().setBody(responseDispatcher.readMockJson("listDatasets"))
    })
    val dsnList = zosDsnList.listDsn("TEST.**.TEST1", listParams)
    responseDispatcher.clearValidationList()
    Assertions.assertEquals(4, dsnList.items.size)
  }

  @Test
  fun testListDsnMembers () {
    val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
    val listParams = ListParams()
    val zosDsnList = ZosDsnList(conn, proxyClient)
    responseDispatcher.injectEndpoint({
      it?.path?.matches(Regex("http://.*/zosmf/restfiles/ds.*")) == true
    }, {
      MockResponse().setBody(responseDispatcher.readMockJson("listDatasetMembers"))
    })
    val dsnMembersList = zosDsnList.listDsnMembers("SYS1.PROCLIB", listParams)
    Assertions.assertEquals(87, dsnMembersList.items.size)
    responseDispatcher.clearValidationList()
  }
}
