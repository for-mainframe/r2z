// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

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
  val moreRows: Boolean? = null,

  @SerializedName("JSONversion")
  @Expose
  val jsonVersion: Int = 0
)
