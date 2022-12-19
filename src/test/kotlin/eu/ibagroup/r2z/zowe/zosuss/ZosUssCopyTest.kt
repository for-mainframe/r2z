// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosfiles

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosuss.ZosUssCopy
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZosUssCopyTest {
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
    fun testCopyFile() {
        val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
        val zosUssCopy = ZosUssCopy(conn, proxyClient)
        responseDispatcher.injectEndpoint(
            {
                it?.requestLine?.matches(Regex("PUT http://.*/zosmf/restfiles/fs/u/IJMP/destFile HTTP/.*")) == true
            }, {
                MockResponse().setResponseCode(204)
            }
        )
        val response = zosUssCopy.copy("/u/IJMP/fileToCopy", "/u/IJMP/destFile", replace = true)
        Assertions.assertEquals(204, response.code())

        responseDispatcher.clearValidationList()
    }

    @Test
    fun testCopyFileToDS() {
        val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
        val zosUssCopy = ZosUssCopy(conn, proxyClient)
        responseDispatcher.injectEndpoint(
            {
                it?.requestLine?.matches(Regex("PUT http://.*/zosmf/restfiles/ds/IJMP.TEST.DATA HTTP/.*")) == true
            }, {
                MockResponse().setResponseCode(204)
            }
        )
        val response = zosUssCopy.copyToDS("/u/IJMP/fileToCopy", "IJMP.TEST.DATA", replace = true)
        Assertions.assertEquals(204, response.code())

        responseDispatcher.clearValidationList()
    }

    @Test
    fun testCopyFileToMember() {
        val conn = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
        val zosUssCopy = ZosUssCopy(conn, proxyClient)
        responseDispatcher.injectEndpoint(
            {
                it?.requestLine?.matches(Regex("PUT http://.*/zosmf/restfiles/ds/IJMP.TEST.DATA\\(TESTMEM\\) HTTP/.*")) == true
            }, {
                MockResponse().setResponseCode(204)
            }
        )
        val response = zosUssCopy.copyToMember("/u/IJMP/fileToCopy", "IJMP.TEST.DATA", "TESTMEM", replace = true)
        Assertions.assertEquals(204, response.code())

        responseDispatcher.clearValidationList()
    }
}
