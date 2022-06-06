/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright © 2021 IBA Group, a.s.
 */

package eu.ibagroup.r2z.zowe.examples

import eu.ibagroup.r2z.zowe.config.getAuthEncoding
import eu.ibagroup.r2z.zowe.config.parseConfigJson
import eu.ibagroup.r2z.zowe.config.withBasicPrefix

fun main() {
  val inputStream = object {}.javaClass.classLoader.getResourceAsStream("zowe.config.json")
  if (inputStream != null) {
    val zoweConfig = parseConfigJson(inputStream)
    println("url=\"${zoweConfig.protocol}://${zoweConfig.host}:${zoweConfig.port}\"; username=${zoweConfig.user}; password=${zoweConfig.password}")
    println(zoweConfig.getAuthEncoding().withBasicPrefix())
  }
}
