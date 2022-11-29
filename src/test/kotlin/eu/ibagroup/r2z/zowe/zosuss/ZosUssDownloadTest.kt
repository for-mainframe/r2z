// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosfiles

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.ZosDsnDownload
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input.DownloadParams
import eu.ibagroup.r2z.zowe.client.sdk.zosuss.ZosUssFileDownload
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZosUssDownloadTest {
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
    fun testDownloadDsn() {
        val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
        val zosUssFileDownload = ZosUssFileDownload(conn, proxyClient)

        val responseBody = javaClass.classLoader.getResource("mock/downloadDsnMember.txt")?.readText()  ?: ""
        responseDispatcher.injectEndpoint(
            {
                it?.requestLine?.matches(Regex("GET http://.*/zosmf/restfiles/fs/u/IJMP/text.txt HTTP/.*")) == true
            },
            { MockResponse().setBody(responseBody) }
        )
        val stream = zosUssFileDownload.retrieveContents("/u/IJMP/text.txt")
        val content = stream.bufferedReader().use { it.readText() }
        Assertions.assertEquals(responseBody, content)

        responseDispatcher.clearValidationList()
    }

}
