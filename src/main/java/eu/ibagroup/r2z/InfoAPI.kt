package eu.ibagroup.r2z

import retrofit2.Call
import retrofit2.http.GET

interface InfoAPI {

  @GET("zosmf/info")
  fun getSystemInfo() : Call<InfoResponse>

}