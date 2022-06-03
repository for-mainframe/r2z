/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright © 2021 IBA Group, a.s.
 */

package eu.ibagroup.r2z.zowe.config

import com.google.gson.*
import org.yaml.snakeyaml.Yaml
import com.starxg.keytar.Keytar
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

const val WINDOWS_MAX_PASSWORD_LENGTH = 2560

// TODO: doc
fun String.encodeToBase64(charset: Charset = Charsets.UTF_8): String
    = Base64.getEncoder().encodeToString(this.toByteArray(charset))

// TODO: doc
fun String.decodeFromBase64(charset: Charset = Charsets.UTF_8): String = String(Base64.getDecoder().decode(this), charset)

// TODO: doc
fun String.withBasicPrefix () = "Basic $this"

// TODO: doc
fun ZoweConnection.getAuthEncoding (): String {
  if (port == null || host?.isEmpty() != false || password?.isEmpty() != false || user?.isEmpty() != false) {
    throw IllegalStateException("Connection data not setup properly")
  }
  return "$user:$password".encodeToBase64()
}

// TODO: doc
fun ZoweConfig.getAuthEncoding (): String {
  if (host?.isEmpty() != false || port == null || user?.isEmpty() != false || password?.isEmpty() != false) {
    throw IllegalStateException("Connection data not setup properly")
  }
  return "$user:$password".encodeToBase64()
}

// TODO: doc
fun parseConfigYaml (inputStream: InputStream): ZoweConnection {
  val loaded: Map<String, Any> = Yaml().load(inputStream)
  return ZoweConnection(
    loaded["host"] as String?,
    loaded["port"] as Int?,
    loaded["user"] as String?,
    loaded["password"] as String?,
    loaded["rejectUnauthorized"] as Boolean? ?: false,
    loaded["protocol"] as String? ?: "http",
    loaded["basePath"] as String? ?: "/",
    loaded["encoding"] as Int? ?: 1047,
    loaded["responseTimeout"] as Int? ?: 600
  )
}

// TODO: doc
fun parseConfigYaml (configString: String): ZoweConnection = parseConfigYaml(ByteArrayInputStream(configString.toByteArray()))

// TODO: doc
private fun formProfiles (profiles: Map<String, ZoweConfigProfile>?) {
  profiles?.forEach { (_, v) ->
    v.properties?.forEach{ (propKey, propValue) ->
      if (propValue is Double? && propValue == propValue?.toLong()?.toDouble()) {
        v.properties[propKey] = propValue?.toLong()
      }
    }
    v.profiles?.forEach{ (_, childProfile) -> childProfile.parentProfile = v }
    formProfiles(v.profiles)
  }
}

/**
 * Parses json string to ZoweConfig object model.
 * @param configString - json string with zowe config.
 * @return ZoweConfig object model.
 */
fun parseConfigJson(configString: String): ZoweConfig {
  val zoweConfig = Gson().fromJson(configString, ZoweConfig::class.java)
  formProfiles(zoweConfig.profiles)
  return zoweConfig
}

/**
 * Reads input stream and parse it to ZoweConfig object model.
 * @param configString - stream with json string of zowe config.
 * @return ZoweConfig object model.
 */
fun parseConfigJson (inputStream: InputStream): ZoweConfig = parseConfigJson(String(inputStream.readBytes()))

/**
 * Represents API to interact with secure storage.
 * @author Valiantsin Krus
 * @version 0.5
 * @since 2021-08-12
 */
interface KeytarWrapper {
  /**
   * Returns a password by service name and account.
   * @param service - service name.
   * @param account - account name.
   * @return extracted password.
   */
  fun getPassword(service: String, account: String): String

  /**
   * Updates or creates password for account of service.
   * @param service - service name.
   * @param account - account name
   * @return Nothing.
   */
  fun setPassword(service: String, account: String, password: String)

  /**
   * Removes credentials for account in service. If account in service is single than it removes a service.
   * @param service - service name.
   * @param account - account name.
   * @return true if success and false otherwise.
   */
  fun deletePassword(service: String, account: String): Boolean

  /**
   * Extracts all credentials for service.
   * @param service - service name.
   * @return Map where key is account name in service and value is password for this account.
   */
  fun getCredentials(service: String): Map<String, String>
}

/**
 * Implements ability to access secure storage of operating system using library com.starxg.java-keytar.
 * It uses the following storages depending on the operating system:
 * 1) For Windows - Credential Manager;
 * 2) For Mac OS - Keychain;
 * 3) For Linux - Secret Service API/libsecret.
 * @author Valiantsin Krus
 * @version 0.5
 * @since 2021-08-12
 */
open class DefaultKeytarWrapper: KeytarWrapper{

  private val keytar = Keytar.getInstance()

  /**
   * @see KeytarWrapper.getPassword
   */
  override fun getPassword(service: String, account: String): String = keytar.getPassword(service, account)

  /**
   * @see KeytarWrapper.setPassword
   */
  override fun setPassword(service: String, account: String, password: String) {
    keytar.setPassword(service, account, password)
  }

  /**
   * @see KeytarWrapper.deletePassword
   */
  override fun deletePassword(service: String, account: String): Boolean = keytar.deletePassword(service, account)

  /**
   * @see KeytarWrapper.getCredentials
   */
  override fun getCredentials(service: String): Map<String, String> = keytar.getCredentials(service)

}
