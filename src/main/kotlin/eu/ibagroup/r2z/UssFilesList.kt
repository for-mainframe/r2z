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

data class UssFilesList(
  @SerializedName("items")
  @Expose
  val items: List<UssFile> = emptyList(),

  @SerializedName("returnedRows")
  @Expose
  val returnedRows: Int = 0,

  @SerializedName("totalRows")
  @Expose
  val totalRows: Int? = null,

  @SerializedName("moreRows")
  @Expose
  val moreRows: Int? = null,

  @SerializedName("JSONversion")
  @Expose
  val jsonVersion: Int = 0
)