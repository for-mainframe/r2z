// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.zosuss

import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosfiles.ZosDsn
import eu.ibagroup.r2z.zowe.client.sdk.zosuss.ZosUssFile
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.concurrent.thread

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZosUssFileTest {
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
    fun deleteFile() {
        val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
        val zosUssFile = ZosUssFile(connection, proxyClient)

        responseDispatcher.injectEndpoint(
            {
                it?.requestLine?.matches(Regex("DELETE http://.*/zosmf/restfiles/fs/u/IJMP/text.txt HTTP/.*")) == true
            },
            { MockResponse().setResponseCode(204) }
        )
        val response = zosUssFile.deleteFile("/u/IJMP/text.txt")
        Assertions.assertEquals(204, response.code())

        responseDispatcher.clearValidationList()
    }

    @Test
    fun createUssFile() {
        val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
        val zosUssFile = ZosUssFile(connection, proxyClient)
        val params = CreateUssFile(
            type = FileType.FILE,
            mode = FileMode(7,7,7)
        )

        responseDispatcher.injectEndpoint(
            {
                it?.requestLine?.matches(Regex("POST http://.*/zosmf/restfiles/fs/u/IJMP/text.txt HTTP/.*")) == true //&&
                        //Gson().fromJson(String(it.body), CreateUssFile::class.java) == params
            },
            { MockResponse().setResponseCode(201) }
        )
        val response = zosUssFile.createFile("/u/IJMP/text.txt", params)
        Assertions.assertEquals(201, response.code())

        responseDispatcher.clearValidationList()
    }

    @Test
    fun createUssDir() {
        val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
        val zosUssFile = ZosUssFile(connection, proxyClient)
        val params = CreateUssFile(
            type = FileType.DIR,
            mode = FileMode(7,7,7)
        )

        responseDispatcher.injectEndpoint(
            {
                it?.requestLine?.matches(Regex("POST http://.*/zosmf/restfiles/fs/u/IJMP/newdir HTTP/.*")) == true //&&
                //Gson().fromJson(String(it.body), CreateUssFile::class.java) == params
            },
            { MockResponse().setResponseCode(201) }
        )
        val response = zosUssFile.createFile("/u/IJMP/newdir", params)
        Assertions.assertEquals(201, response.code())

        responseDispatcher.clearValidationList()
    }

    @Test
    fun writeToFile() {
        val connection = ZOSConnection(TEST_HOST, TEST_PORT, TEST_USER, TEST_PASSWORD, "http")
        val zosUssFile = ZosUssFile(connection, proxyClient)
        val text = "Hello There!"

        responseDispatcher.injectEndpoint(
            {
                it?.requestLine?.matches(Regex("PUT http://.*/zosmf/restfiles/fs/u/IJMP/text.txt HTTP/.*")) == true
            },
            { MockResponse().setResponseCode(201) }
        )
        val response = zosUssFile.writeToFile("/u/IJMP/text.txt", text.toByteArray())
        Assertions.assertEquals(201, response.code())

        responseDispatcher.clearValidationList()
    }
}

