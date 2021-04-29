package eu.ibagroup.r2z

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import ibagroup.eu.r2z.CanJobPrgOutRequest
import ibagroup.eu.r2z.HoldJobRequest
import ibagroup.eu.r2z.ReleaseJobRequest
import ibagroup.eu.r2z.SubmitJobRequest
import okhttp3.ResponseBody
import okio.ByteString
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.lang.IllegalArgumentException

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

  @GET("/zosmf/restjobs/jobs")
  fun getFilteredJobs(
    @Header("Authorization") basicCredentials: String,
    @Query("owner") owner: String? = null,
    @Query("prefix") prefix: String? = null,
    @Query("jobid") jobId: String? = null,
    @Query("max-jobs") maxCount: Int? = null,
    @Query("user-correlator") userCorrelator: String? = null
  ): Call<List<JobStatus>>

  @GET("/zosmf/restjobs/jobs/{job-name}/{job-id}/files")
  fun getJobSpoolFiles(
    @Header("Authorization") basicCredentials: String,
    @Path("job-name") jobName: String,
    @Path("job-id") jobId: String
  ): Call<List<SpoolFile>>

  @GET("/zosmf/restjobs/jobs/{job-correlator}/files")
  fun getJobSpoolFiles(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
  ): Call<List<SpoolFile>>


  @GET("/zosmf/restjobs/jobs/{job-name}/{job-id}/files/{file-id}/records")
  fun getSpoolFileRecords(
    @Header("Authorization") basicCredentials: String,
    @Path("job-name") jobName: String,
    @Path("job-id") jobId: String,
    @Path("file-id") fileId: Int,
    @Query("mode") mode: BinaryMode = BinaryMode.TEXT,
    @Header("X-IBM-Record-Range") range: RecordRange? = null
    ): Call<ByteArray>

  @GET("/zosmf/restjobs/jobs/{job-correlator}/files/{file-id}/records")
  fun getSpoolFileRecords(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Path("file-id") fileId: Int,
    @Query("mode") mode: BinaryMode = BinaryMode.TEXT,
    @Header("X-IBM-Record-Range") range: RecordRange? = null
  ): Call<ByteArray>

  @GET("/zosmf/restjobs/jobs/{job-name}/{job-id}/files/JCL/records")
  fun getJCLRecords(
    @Header("Authorization") basicCredentials: String,
    @Path("job-name") jobName: String,
    @Path("job-id") jobId: String,
    @Query("mode") mode: BinaryMode = BinaryMode.TEXT,
    @Header("X-IBM-Record-Range") range: RecordRange? = null
  ): Call<ByteArray>

  @GET("/zosmf/restjobs/jobs/{job-correlator}/files/JCL/records")
  fun getJCLRecords(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Query("mode") mode: BinaryMode = BinaryMode.TEXT,
    @Header("X-IBM-Record-Range") range: RecordRange? = null
  ): Call<ByteArray>

  @PUT("/zosmf/restjobs/jobs/{jobname}/{jobid}")
  fun holdJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("jobname") jobName: String,
    @Path("jobid") jobId: String,
    @Body body: JsonObject
  ): Call<HoldJobRequest>

  @PUT("/zosmf/restjobs/jobs/{job-correlator}")
  fun holdJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Body body: JsonObject
  ): Call<HoldJobRequest>

  @PUT("/zosmf/restjobs/jobs/{jobname}/{jobid}")
  fun releaseJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("jobname") jobName: String,
    @Path("jobid") jobId: String,
    @Body body: JsonObject
  ): Call<ReleaseJobRequest>

  @PUT("/zosmf/restjobs/jobs/{job-correlator}")
  fun releaseJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Body body: JsonObject
  ): Call<ReleaseJobRequest>

  @DELETE("/zosmf/restjobs/jobs/{jobname}/{jobid}")
  fun canJobPrgOutRequest(
    @Header("Authorization") basicCredentials: String,
    @Header("X-IBM-Job-Modify-Version") version : String,
    @Path("jobname") jobName: String,
    @Path("jobid") jobId: String
  ): Call<CanJobPrgOutRequest>

  @DELETE("/zosmf/restjobs/jobs/{job_correlator}")
  fun canJobPrgOutRequest(
    @Header("Authorization") basicCredentials: String,
    @Header("X-IBM-Job-Modify-Version") version : String,
    @Path("job-correlator") jobId: String
  ): Call<CanJobPrgOutRequest>

  @PUT("/zosmf/restjobs/jobs")
  fun submitJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Header("X-IBM-Inrdr-Mode") mode: String,
    @Body body: String
  ): Call<SubmitJobRequest>

  @PUT("/zosmf/restjobs/jobs")
  fun submitJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Body body: JsonObject
  ): Call<SubmitJobRequest>
}

enum class UseStepData(val value:String) {
  ENABLE("Y"),
  DISABLE("N");


  override fun toString(): String {
    return value
  }
}

enum class BinaryMode(val value: String) {
  BINARY("binary"),
  RECORD("record"),
  TEXT("text");

  override fun toString(): String {
    return value
  }
}

class RecordRange private constructor(var start: Int? = null, var end: Int? = null){
  companion object Factory{
    fun withBounds(start: Int, end: Int): RecordRange {
      if(start<0 || start > 999 || end < 0 || end > 999 || start > end)
        throw IllegalArgumentException("Illegal bounds for record range. Correct boundaries is [0 ... 999].")
      return RecordRange(start, end)
    }
    fun withOffset(start: Int, offset: Int): RecordRange = withBounds(start, start + offset)
  }

  override fun toString(): String {
    return "$start-$end"
  }

}