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

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import java.io.File
import java.lang.IllegalStateException
import kotlin.collections.ArrayList

/**
 * Represents an object model of zowe.config.json file.
 * @author Valiantsin Krus
 * @version 0.5
 * @since 2021-08-12
 */
class ZoweConfig(
  @SerializedName("\$schema")
  private val schema: String,
  val profiles: Map<String, ZoweConfigProfile>,
  val defaults: Map<String, String>
) {

  companion object {
    val ZOWE_SECURE_ACCOUNT = "secure_config_props"
    val ZOWE_SERVICE_BASE = "Zowe"
    val ZOWE_SERVICE_NAME = "$ZOWE_SERVICE_BASE/$ZOWE_SECURE_ACCOUNT"
  }

  /**
   * Builder class for setting the sequence of profiles to search property by name.
   * @param propName property name to search.
   */
  inner class PropertyBuilder(val propName: String) {
    var profilesToSearchProp = mutableListOf<ZoweConfigProfile?>()

    /**
     * Sets the next profile to search for the property to zosmf.
     * @return Nothing.
     */
    fun zosmf() {
      profilesToSearchProp.add(zosmfProfile)
    }

    /**
     * Sets the next profile to search for the property to tso.
     * @return Nothing
     */
    fun tso() {
      profilesToSearchProp.add(tsoProfile)
    }


    /**
     * Sets the next profile to search for the property to ssh.
     * @return Nothing
     */
    fun ssh () {
      profilesToSearchProp.add(sshProfile)
    }

    /**
     * Sets the next profile to search for the property to base.
     * @return Nothing
     */
    fun base () {
      profilesToSearchProp.add(baseProfile)
    }
  }

  /**
   * Searches for a property by a given sequence of profiles.
   * @return Found property value or null.
   */
  private fun PropertyBuilder.search (): Any? {
    val profiles = profilesToSearchProp.filterNotNull()
    for (profile in profiles) {
      var property = profile.properties?.get(propName)
      if (property == null) {
        property = profile.parentProfile?.properties?.get(propName)
      }
      return property ?: continue
    }
    return null
  }

  /**
   * Searches where to set property value and sets it after searching by given profiles sequence.
   * For example if given profiles sequence is zosmf -> base then it checks that zosmf profile
   * or its parent contains property and if so, then method will set property in this profile else
   * method will try to find property in base profile and set it there etc. If profile with
   * corresponding property not found then nothing will happen.
   */
  fun PropertyBuilder.set (value: Any?) {
    val profiles = profilesToSearchProp.filterNotNull()
    for (profile in profiles) {
      if (profile.properties?.containsKey(propName) == true) {
        profile.properties[propName] = value
      }
      if (profile.parentProfile?.properties?.containsKey(propName) == true) {
        profile.parentProfile?.properties?.set(propName, value)
      }
    }
  }

  /**
   * Searches for a property with creating profiles sequence to search.
   * @see search
   * @param propName property name to search.
   * @param block extension function for PropertyBuilder class. This parameter is needed for
   *              creating a sequence of profiles to search by invoking corresponding methods
   *              in the right order.
   * @return Property value.
   */
  fun searchProperty (propName: String, block: PropertyBuilder.() -> Unit): Any? {
    return PropertyBuilder(propName).apply(block).search()
  }

  /**
   * Searches for a property and updates it in found profile with creating profiles sequence to search.
   * @see set
   * @param propName property name to search.
   * @param block extension function for PropertyBuilder class. This parameter is needed for
   *              creating a sequence of profiles to search by invoking corresponding methods
   *              in the right order.
   * @return Nothing.
   */
  fun updateProperty (propName: String, propValue: Any?, block: PropertyBuilder.() -> Unit) {
    PropertyBuilder(propName).apply(block).set(propValue)
  }

  /**
   * Extracts and decodes config object of all files from credential storage.
   * @see KeytarWrapper
   * @see DefaultKeytarWrapper
   * @param keytar instance of [KeytarWrapper]. This param is needed for accessing credential storage.
   * @return Map where key is config file path and value is map of secure properties.
   *         For example:
   *         {
   *           "/user/root/zowe.config.json": {
   *              "profiles.base.properties.user": "testUser",
   *              "profiles.base.properties.password": "testPasswird",
   *           }
   *         }
   */
  private fun readZoweCredentialsFromStorage (keytar: KeytarWrapper = DefaultKeytarWrapper()): Map<*, *> {
    var configMap = keytar.getCredentials(ZOWE_SERVICE_BASE)
    if (configMap.isNotEmpty() && configMap.containsKey(ZOWE_SECURE_ACCOUNT)) {
      return Gson().fromJson(configMap[ZOWE_SECURE_ACCOUNT]?.decodeFromBase64(), Map::class.java)
    }
    var result = ""
    var configNumber = 1
    do {
      configMap = keytar.getCredentials("${ZOWE_SERVICE_NAME}-${configNumber}")
      val account = "${ZOWE_SECURE_ACCOUNT}-${configNumber++}"
      if (configMap.containsKey(account)) {
        result += configMap[account]
      }
    } while (!configMap.isEmpty())
    return Gson().fromJson(result.decodeFromBase64(), Map::class.java)
  }

  /**
   * Extracts secure properties from secure store by zowe config file path in current instance.
   * @see readZoweCredentialsFromStorage
   * @param filePath path of zowe.config.json file. Secure props will be extracted by this parameter.
   * @param keytar instance of [KeytarWrapper]. This param is needed for accessing credential storage.
   * @return Nothing.
   */
  fun extractSecureProperties (filePath: String, keytar: KeytarWrapper = DefaultKeytarWrapper()) {
    val configCredentials = readZoweCredentialsFromStorage(keytar).toMutableMap()
    if (configCredentials.containsKey(filePath)) {
      @Suppress("UNCHECKED_CAST")
      val configCredentialsMap = configCredentials[filePath] as Map<String, Any>
      this.profiles.forEach {(profileName, profile) ->
        profile.secure?.forEach { secureProfileProp ->
          profile.properties?.set(secureProfileProp,
            configCredentialsMap["profiles.${profileName}.properties.${secureProfileProp}"]
          )
        }
      }
    }
  }

  /**
   * Updates secure object for provided file in credential object and save these changes to credential storage.
   * @see readZoweCredentialsFromStorage
   * @param filePath path of zowe.config.json file. Secure props will be saved
   *                 inside this property of connection object.
   * @param keytar instance of [KeytarWrapper]. This param is needed for accessing credential storage.
   * @return Nothing.
   */
  fun saveSecureProperties (filePath: String, keytar: KeytarWrapper = DefaultKeytarWrapper()) {
    val configCredentials = readZoweCredentialsFromStorage(keytar).toMutableMap()
    val configCredentialsMap = mutableMapOf<String, Any?>()
    this.profiles.forEach { (profileName, profile) ->
      profile.secure?.forEach { propName ->
        if (profile.properties?.containsKey(propName) == true) {
          configCredentialsMap["profiles.${profileName}.properties.${propName}"] = profile.properties[propName]
        }
      }
    }
    configCredentials[filePath] = configCredentialsMap
    val passwordToSave = Gson().toJson(configCredentials)
    val osName = System.getProperty("os.name")
    val encodedObjectToSave = passwordToSave.encodeToBase64()
    if (passwordToSave.length < WINDOWS_MAX_PASSWORD_LENGTH || !osName.contains("Windows")) {
      keytar.setPassword(ZOWE_SERVICE_BASE, ZOWE_SECURE_ACCOUNT, encodedObjectToSave)
    } else {
      keytar.deletePassword(ZOWE_SERVICE_BASE, ZOWE_SECURE_ACCOUNT)
      encodedObjectToSave.chunked(WINDOWS_MAX_PASSWORD_LENGTH).forEachIndexed { i, chunk ->
        keytar.setPassword(ZOWE_SERVICE_BASE, "$ZOWE_SECURE_ACCOUNT-${i + 1}", chunk)
      }
    }
  }

  /**
   * Extracts secure properties from secure store by zowe config file path in current instance.
   * @see readZoweCredentialsFromStorage
   * @param filePathTokens path of zowe.config.json file splitted by delimiter.
   *                       Secure props will be extracted by this parameter.
   * @param keytar instance of [KeytarWrapper]. This param is needed for accessing credential storage.
   * @return Nothing.
   */
  fun extractSecureProperties (filePathTokens: Array<String>, keytar: KeytarWrapper = DefaultKeytarWrapper()) {
    extractSecureProperties(filePathTokens.joinToString(File.separator), keytar)
  }

  /**
   * Updates secure object for provided file in credential object and save these changes to credential storage.
   * @see readZoweCredentialsFromStorage
   * @param filePath path of zowe.config.json file splitted by delimiter.
   *                 Secure props will be saved inside this property of connection object.
   * @param keytar instance of [KeytarWrapper]. This param is needed for accessing credential storage.
   * @return Nothing.
   */
  fun saveSecureProperties (filePathTokens: Array<String>, keytar: KeytarWrapper = DefaultKeytarWrapper()) {
    saveSecureProperties(filePathTokens.joinToString(File.separator), keytar)
  }

  /**
   * Deserializes current [ZoweConfig] instance to JSON string without secure properties.
   * @return String with deserialized object.
   */
  fun toJson (): String {
    val gson = Gson()
    val zoweConfigCopy = parseConfigJson(gson.toJson(this))
    zoweConfigCopy.profiles.forEach { (_, profile) ->
      profile.secure?.forEach { propName ->
        profile.properties?.remove(propName)
      }
    }
    return GsonBuilder().setPrettyPrinting().create().toJson(zoweConfigCopy, zoweConfigCopy::class.java)
  }

  /**
   * Creates [ZOSConnection] based on zowe config or throws exception if data is not correct.
   * @return [ZOSConnection] instance
   */
  fun toZosConnection(): ZOSConnection {
    if (host?.isEmpty() != false || port == null || user?.isEmpty() != false || password == null || protocol.isEmpty()){
      throw IllegalStateException("Zowe config data is not valid for creating ZOSConnection")
    }
    return ZOSConnection(host ?: "", port.toString(), user ?: "", password ?: "", protocol)
  }

  var user: String?
    get() = searchProperty("user") { zosmf(); base() } as String?
    set(el) { updateProperty("user", el ?: "") { zosmf(); base() } }

  var password: String?
    get() = searchProperty("password") { zosmf(); base() } as String?
    set(el) { updateProperty("password", el ?: "") { zosmf(); base() } }

  var host: String?
    get() = searchProperty("host") { zosmf(); base() } as String?
    set(el) { updateProperty("host", el) { zosmf(); base() } }

  var rejectUnauthorized: Boolean?
    get() = searchProperty("rejectUnauthorized") { zosmf(); base() } as Boolean?
    set(el) { updateProperty("rejectUnauthorized", el ?: true) { zosmf(); base() } }

  var port: Long?
    get() = searchProperty("port") { zosmf(); base() } as Long?
    set(el) { updateProperty("port", el) { zosmf(); base() } }

  var protocol: String
    get() = searchProperty("protocol") { zosmf(); base() } as String? ?: "https"
    set(el) { updateProperty("protocol", el) { zosmf(); base() } }

  var basePath: String
    get() = searchProperty("basePath") { zosmf(); base() } as String? ?: "/"
    set(el) { updateProperty("basePath", el) { zosmf(); base() } }

  var encoding: Long
    get() = searchProperty("encoding") { zosmf(); base() } as Long? ?: 1047
    set(el) { updateProperty("encoding", el) { zosmf(); base() } }

  var responseTimeout: Long
    get() = searchProperty("responseTimeout") { zosmf(); base() } as Long? ?: 600
    set(el) { updateProperty("responseTimeout", el) { zosmf(); base() } }

  /**
   * Searches profile by its path. For example if profile has path "gr1.example" then it will search
   * profile "example" in "gr1" group.
   * @param searchPath path to search profile
   * @return found profile or null if *searchPath* is not valid or no one profile exists by this path
   */
  fun profile(searchPath: String?): ZoweConfigProfile? {
    searchPath ?: return null
    val searchPaths = searchPath.split(".")
    searchPaths.isEmpty() && return null
    return searchPaths.drop(1).fold(profiles[searchPaths[0]]) { acc, s ->
      acc?.let { if (it.profiles?.containsKey(s) == true) it.profiles[s] else null }
    }
  }

  val zosmfProfile: ZoweConfigProfile?
    get() = profile(defaults["zosmf"])

  val tsoProfile: ZoweConfigProfile?
    get() = profile(defaults["tso"])

  val sshProfile: ZoweConfigProfile?
    get() = profile(defaults["ssh"])

  val baseProfile: ZoweConfigProfile?
    get() = profile(defaults["base"])
}

class ZoweConfigProfile(
  val type: String,
  val properties: MutableMap<String, Any?>?,
  val secure: ArrayList<String>?,
  val profiles: Map<String, ZoweConfigProfile>?,
  var parentProfile: ZoweConfigProfile?
)
