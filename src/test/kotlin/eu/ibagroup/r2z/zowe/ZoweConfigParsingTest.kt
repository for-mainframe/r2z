/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright © 2021 IBA Group, a.s.
 */

package eu.ibagroup.r2z.zowe

import okhttp3.mockwebserver.MockWebServer
import eu.ibagroup.r2z.zowe.config.*
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*
import java.net.InetSocketAddress
import java.net.Proxy

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZoweConfigParsingTest: ZoweConfigTestBase() {

  lateinit var mockServer: MockWebServer
  lateinit var proxyClient: OkHttpClient
  lateinit var keytarWrapper: KeytarWrapper
  lateinit var zoweConfig: ZoweConfig

  @BeforeAll
  fun createMockServer () {
    keytarWrapper = DefaultMockKeytarWrapper()
    mockServer = MockWebServer()
    mockServer.dispatcher = MockResponseDispatcher()
    mockServer.start()
    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(mockServer.hostName, mockServer.port))
    proxyClient = OkHttpClient.Builder().proxy(proxy).build()
  }

  @AfterAll
  fun stopMockServer () {
    mockServer.shutdown()
  }

  @BeforeEach
  fun uploadConfig () {
    zoweConfig = parseConfigJson(stringConfigJson)
    zoweConfig.extractSecureProperties(TEST_ZOWE_CONFIG_PATH, keytarWrapper)
  }

  @Test
  fun testParsingJsonString() {
    checkZoweConfig(zoweConfig)
  }

  @Test
  fun testParsingJsonStream() {
    val zoweConfig = parseConfigJson(streamConfigJson)
    zoweConfig.extractSecureProperties(TEST_ZOWE_CONFIG_PATH, keytarWrapper)
    checkZoweConfig(zoweConfig)
  }

  @Test
  fun testParsingYamlString() {
    val zoweConnection = parseConfigYaml(stringConfigYaml)
    checkZoweConnection(zoweConnection)
  }

  @Test
  fun testParsingYamlStream() {
    val zoweConnection = parseConfigYaml(streamConfigYaml)
    checkZoweConnection(zoweConnection)
  }

  @Test
  fun testAuthTokenCreation() {
    zoweConfig.user = null
    Assertions.assertThrows(IllegalStateException::class.java){ zoweConfig.getAuthEncoding() }
    zoweConfig.user = "user"
    zoweConfig.password = null
    Assertions.assertThrows(IllegalStateException::class.java){ zoweConfig.getAuthEncoding() }
    zoweConfig.password = "password"
    zoweConfig.host = ""
    Assertions.assertThrows(IllegalStateException::class.java){ zoweConfig.getAuthEncoding() }
    zoweConfig.host = "example.host"
    zoweConfig.port = null
    Assertions.assertThrows(IllegalStateException::class.java){ zoweConfig.getAuthEncoding() }
    zoweConfig.port = 443
    Assertions.assertDoesNotThrow { zoweConfig.getAuthEncoding() }
  }

  fun checkZoweConfig(zoweConfig: ZoweConfig) {
    Assertions.assertEquals(zoweConfig.user, TEST_USER)
    Assertions.assertEquals(zoweConfig.password, TEST_PASSWORD)
    Assertions.assertEquals(zoweConfig.host, "example.host1")
    Assertions.assertEquals(zoweConfig.rejectUnauthorized, true)
    Assertions.assertEquals(zoweConfig.port, 10443)
    Assertions.assertEquals(zoweConfig.protocol, "https")
    Assertions.assertEquals(zoweConfig.basePath, "/")
    Assertions.assertEquals(zoweConfig.encoding, 1047)
    Assertions.assertEquals(zoweConfig.responseTimeout, 600)
  }

  fun checkZoweConnection(zoweConnection: ZoweConnection) {
    Assertions.assertEquals(zoweConnection.user, "exampleUser")
    Assertions.assertEquals(zoweConnection.password, "examplePassword")
    Assertions.assertEquals(zoweConnection.host, "host.example")
    Assertions.assertEquals(zoweConnection.rejectUnauthorized, false)
    Assertions.assertEquals(zoweConnection.port, 10443)
    Assertions.assertEquals(zoweConnection.protocol, "https")
    Assertions.assertEquals(zoweConnection.basePath, "/")
    Assertions.assertEquals(zoweConnection.encoding, 1047)
    Assertions.assertEquals(zoweConnection.responseTimeout, 600)
  }
}
