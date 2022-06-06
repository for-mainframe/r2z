// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe

import com.google.gson.Gson
import eu.ibagroup.r2z.zowe.config.KeytarWrapper
import eu.ibagroup.r2z.zowe.config.ZoweConfig
import eu.ibagroup.r2z.zowe.config.encodeToBase64
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import java.io.InputStream
import java.util.*

val TEST_HOST = "example.host"
val TEST_PORT = "443"
val TEST_USER = "testUser"
val TEST_PASSWORD = "testPassword"
val TEST_ZOWE_CONFIG_PATH = "/test/zowe/config/path"

open class MockKeytarWrapper: KeytarWrapper {
  override fun getPassword(service: String, account: String): String = ""

  override fun setPassword(service: String, account: String, password: String) {}

  override fun deletePassword(service: String, account: String): Boolean = true

  override fun getCredentials(service: String): Map<String, String> = emptyMap()
}

open class DefaultMockKeytarWrapper: MockKeytarWrapper () {

  protected fun createMultiplePasswords (filePaths: Collection<String>, user: String, password: String): String {
    val credentialsMap = mutableMapOf<String, Map<String, Any?>>()
    filePaths.forEach { credentialsMap[it] = mapOf(
      Pair("profiles.base.properties.user", user),
      Pair("profiles.base.properties.password", password)
    ) }
    return Gson().toJson(credentialsMap).encodeToBase64()
  }

  protected fun createSinglePassword (filePath: String, user: String, password: String): String {
    val credentialsMap = mutableMapOf<String, Map<String, Any?>>(Pair(filePath, mapOf(
      Pair("profiles.base.properties.user", user),
      Pair("profiles.base.properties.password", password)
    )))
    return Gson().toJson(credentialsMap).encodeToBase64()
  }
  override fun getCredentials(service: String): Map<String, String> {
    return mapOf(Pair(
      ZoweConfig.ZOWE_SECURE_ACCOUNT,
      createSinglePassword(TEST_ZOWE_CONFIG_PATH, TEST_USER, TEST_PASSWORD)
    ))
  }
}

open class ZoweConfigTestBase {
  lateinit var stringConfigJson: String
  lateinit var stringConfigYaml: String
  lateinit var streamConfigJson: InputStream
  lateinit var streamConfigYaml: InputStream

  val JSON_CONFIG = "zowe.config.json"
  val YAML_CONFIG = "zowe.config.yaml"

  protected fun getResourceTextOrAssertError(resourceName: String): String {
    return javaClass.classLoader.getResource(resourceName)?.readText()
      ?: Assertions.fail("$resourceName cannot be empty.")
  }

  protected fun getResourceStreamOrAssertError(resourceName: String): InputStream {
    return javaClass.classLoader.getResourceAsStream(resourceName)
      ?: Assertions.fail("$resourceName stream cannot be null.")
  }


  @BeforeEach
  fun readConfigs() {
    stringConfigJson = getResourceTextOrAssertError(JSON_CONFIG)
    stringConfigYaml = getResourceTextOrAssertError(YAML_CONFIG)
    streamConfigJson = getResourceStreamOrAssertError(JSON_CONFIG)
    streamConfigYaml = getResourceStreamOrAssertError(YAML_CONFIG)
  }
}
