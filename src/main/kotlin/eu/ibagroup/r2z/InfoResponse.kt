package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InfoResponse (
  @SerializedName("zos_version")
  @Expose
  val zosVersion: String = "null",

  @SerializedName("zosmf_port")
  @Expose
  val zosmfPort: String = "null",

  @SerializedName("zosmf_version")
  @Expose
  val zosmfVersion: String = "null",

  @SerializedName("zosmf_hostname")
  @Expose
  val zosmfHostname: String = "null",

  @SerializedName("plugins")
  @Expose
  val plugins: List<Plugin> = emptyList(),

  @SerializedName("zosmf_saf_realm")
  @Expose
  val zosmfSafRealm: String = "null",

  @SerializedName("zosmf_full_version")
  @Expose
  val zosmfFullVersion: String = "null",

  @SerializedName("api_version")
  @Expose
  val apiVersion: String = "null"
)