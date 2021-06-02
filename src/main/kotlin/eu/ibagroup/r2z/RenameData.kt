// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

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

