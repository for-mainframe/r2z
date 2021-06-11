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

data class RenameData(
  @SerializedName("request")
  @Expose
  var request: String = "rename",

  @SerializedName("from-dataset")
  @Expose
  var fromDataset: FromDataset,

  @SerializedName("enq")
  @Expose
  var enq: Enq? = null


) {
  enum class Enq(private var type: String) {

    @SerializedName("EXCLU")
    EXCLU("EXCLU"),
    @SerializedName("SHRW")
    SHRW("SHRW");

    override fun toString(): String {
      return type
    }


  }

  data class FromDataset(

    @SerializedName("dsn")
    @Expose
    var oldDatasetName: String,

    @SerializedName("member")
    @Expose
    var oldMemberName: String? = null,

    )
}

