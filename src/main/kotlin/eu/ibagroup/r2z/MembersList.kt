package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MembersList(

  @SerializedName("items")
  @Expose
  val items: List<Member> = emptyList(),

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