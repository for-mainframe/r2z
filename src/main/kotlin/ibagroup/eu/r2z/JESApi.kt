package ibagroup.eu.r2z

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface JESApi {

  @GET("/zosmf/restjobs/jobs/{job-name}/{job-id}")
  fun getJobStatus(
    @Header("Authorization") basicCredentials: String,
    @Path("job-name") jobName: String,
    @Path("job-id") jobId: String,
    @Query("step-data") useStepData: UseStepData = UseStepData.DISABLE
  ): Call<JobStatus>

  @GET("/zosmf/restjobs/jobs/{job-correlator}")
  fun getJobStatus(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Query("step-data") useStepData: UseStepData = UseStepData.DISABLE
  ): Call<JobStatus>
}

enum class UseStepData(val value:String) {
  @SerializedName("Y") ENABLE("Y"),
  @SerializedName("N") DISABLE("N");


  override fun toString(): String {
    return value
  }
}