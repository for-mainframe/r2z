/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright © 2021 IBA Group, a.s.
 */

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Plugin(
  @SerializedName("pluginVersion")
  @Expose
  val version: String? = null,

  @SerializedName("pluginDefaultName")
  @Expose
  val defaultName: String? = null,

  @SerializedName("pluginStatus")
  @Expose
  val status: String? = null
)