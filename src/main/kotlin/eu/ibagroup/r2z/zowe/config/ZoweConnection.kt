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

// TODO: doc or remove
data class ZoweConnection(
  var host: String?,
  var port: Int?,
  var user: String?,
  var password: String?,
  var rejectUnauthorized: Boolean = true,
  var protocol: String = "http",
  var basePath: String = "/",
  var encoding: Int? = 1047,
  var responseTimeout: Int = 600
)
