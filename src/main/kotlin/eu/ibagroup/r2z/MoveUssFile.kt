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

import com.google.gson.annotations.SerializedName

data class MoveUssFile(
  @SerializedName("request")
  var request: String = "move",

  @SerializedName("from")
  var from: String,

  @SerializedName("overwrite")
  var overwrite: Boolean? = null
)