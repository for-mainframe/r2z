// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.GsonBuilder
import eu.ibagroup.r2z.zowe.*
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Proxy

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InfoAPITest {
  lateinit var mockServer: MockWebServer
  lateinit var proxyClient: OkHttpClient
  lateinit var responseDispatcher: MockResponseDispatcher
  lateinit var infoAPI: InfoAPI

  @BeforeAll
  fun createMockServer() {
    mockServer = MockWebServer()
    responseDispatcher = MockResponseDispatcher()
    mockServer.dispatcher = responseDispatcher
    mockServer.start()
    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(mockServer.hostName, mockServer.port))
    proxyClient = OkHttpClient.Builder().proxy(proxy).build()
    val gson = GsonBuilder().create()
    infoAPI = Retrofit.Builder()
      .baseUrl("http://${TEST_HOST}:${TEST_PORT}")
      .addConverterFactory(GsonConverterFactory.create(gson))
      .client(proxyClient)
      .build()
      .create(InfoAPI::class.java)
  }

  @AfterEach
  fun cleanupTest() {
    responseDispatcher.clearValidationList()
  }

  @AfterAll
  fun stopMockServer() {
    mockServer.shutdown()
  }

  @Test
  fun testPluginsAdapterForArrayResponse() {
    responseDispatcher.injectEndpoint(
      { true },
      { MockResponse().setBody(responseDispatcher.readMockJson("infoResponse") ?: "") }
    )
    responseDispatcher.removeAuthorizationFromZosmfEndpoint("info")

    val response = infoAPI.getSystemInfo().execute()
    Assertions.assertEquals(response.isSuccessful, true)
    val result = response.body() ?: throw Exception("Response for info request should not be empty")
    Assertions.assertEquals(result.plugins.size, 3)
  }

  @Test
  fun testPluginsAdapterForObjectResponse() {
    responseDispatcher.injectEndpoint(
      { true },
      { MockResponse().setBody(responseDispatcher.readMockJson("infoResponsePluginsError") ?: "") }
    )
    responseDispatcher.removeAuthorizationFromZosmfEndpoint("info")

    val response = infoAPI.getSystemInfo().execute()
    Assertions.assertEquals(response.isSuccessful, true)
    val result = response.body() ?: throw Exception("Response for info request should not be empty")
    Assertions.assertEquals(result.plugins.size, 0)
  }
}