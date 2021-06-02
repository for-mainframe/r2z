// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import eu.ibagroup.r2z.annotations.AvailableSince
import eu.ibagroup.r2z.annotations.ZVersion
import retrofit2.Call
import retrofit2.http.*
import java.lang.IllegalArgumentException

interface JESApi {

  @GET("/zosmf/restjobs/jobs/{job-name}/{job-id}")
  fun getJobStatus(
    @Header("Authorization") basicCredentials: String,
    @Path("job-name") jobName: String,
    @Path("job-id") jobId: String,
    @AvailableSince(ZVersion.ZOS_2_2)
    @Query("step-data") useStepData: UseStepData = UseStepData.DISABLE,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("exec-data") execData: ExecData? = null
  ): Call<JobStatus>

  @GET("/zosmf/restjobs/jobs/{job-correlator}")
  fun getJobStatus(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Query("step-data") useStepData: UseStepData = UseStepData.DISABLE,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("exec-data") execData: ExecData? = null
  ): Call<JobStatus>

  @GET("/zosmf/restjobs/jobs")
  fun getFilteredJobs(
    @Header("Authorization") basicCredentials: String,
    @Query("owner") owner: String? = null,
    @Query("prefix") prefix: String? = null,
    @Query("jobid") jobId: String? = null,
    @Query("max-jobs") maxCount: Int? = null,
    @Query("user-correlator") userCorrelator: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("exec-data") execData: ExecData? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("status") status: ActiveStatus? = null
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
    @Header("X-IBM-Record-Range") range: RecordRange? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("fileEncoding") fileEncoding: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("search") searchFor: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("research") searchForRegular: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("insensitive") isSearchCaseSensitive: Boolean? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("maxreturnsize") maxreturnsize: Int? = null
    ): Call<ByteArray>

  @GET("/zosmf/restjobs/jobs/{job-correlator}/files/{file-id}/records")
  fun getSpoolFileRecords(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Path("file-id") fileId: Int,
    @Query("mode") mode: BinaryMode = BinaryMode.TEXT,
    @Header("X-IBM-Record-Range") range: RecordRange? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("fileEncoding") fileEncoding: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("search") searchFor: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("research") searchForRegular: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("insensitive") isSearchCaseSensitive: Boolean? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("maxreturnsize") maxreturnsize: Int? = null
  ): Call<ByteArray>

  @GET("/zosmf/restjobs/jobs/{job-name}/{job-id}/files/JCL/records")
  fun getJCLRecords(
    @Header("Authorization") basicCredentials: String,
    @Path("job-name") jobName: String,
    @Path("job-id") jobId: String,
    @Query("mode") mode: BinaryMode = BinaryMode.TEXT,
    @Header("X-IBM-Record-Range") range: RecordRange? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("fileEncoding") fileEncoding: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("search") searchFor: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("research") searchForRegular: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("insensitive") isSearchCaseSensitive: Boolean? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("maxreturnsize") maxreturnsize: Int? = null
  ): Call<ByteArray>

  @GET("/zosmf/restjobs/jobs/{job-correlator}/files/JCL/records")
  fun getJCLRecords(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Query("mode") mode: BinaryMode = BinaryMode.TEXT,
    @Header("X-IBM-Record-Range") range: RecordRange? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("fileEncoding") fileEncoding: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("search") searchFor: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("research") searchForRegular: String? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("insensitive") isSearchCaseSensitive: Boolean? = null,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Query("maxreturnsize") maxreturnsize: Int? = null
  ): Call<ByteArray>

  @PUT("/zosmf/restjobs/jobs")
  fun submitJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Header("Content-type") contentType: ContentType = ContentType.TEXT_PLAIN,
    @Header("X-IBM-Intrdr-Class") intrdrclass: String? = null,
    @Header("X-IBM-Intrdr-Recfm") recfm: Intrdr_Recfm? = null,
    @Header("X-IBM-Intrdr-Lrecl") lrecl: String? = null,
    @Header("X-IBM-Intrdr-Mode") mode: Intrdr_Mode = Intrdr_Mode.TEXT,
    @Header("X-IBM-User-Correlator") userCorrelator: String? = null,
    @Header("X-IBM-JCL-Symbol-name") symbolName: String? = null,
    @Header("X-IBM-Notification-URL") notificationURL: String? = null,
    @Query("JESB") jesb: String? = null,
    @Body body: String,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Header("X-IBM-Intrdr-FileEncoding") fileEncoding: String? = null
  ): Call<SubmitJobRequest>

  @PUT("/zosmf/restjobs/jobs")
  fun submitJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Header("Content-type") contentType: ContentType = ContentType.APP_JSON,
    @Header("X-IBM-Intrdr-Class") intrdrclass: String? = null,
    @Header("X-IBM-Intrdr-Recfm") recfm: Intrdr_Recfm? = null,
    @Header("X-IBM-Intrdr-Lrecl") lrecl: String? = null,
    @Header("X-IBM-Intrdr-Mode") mode: Intrdr_Mode? = null,
    @Header("X-IBM-User-Correlator") userCorrelator: String? = null,
    @Header("X-IBM-JCL-Symbol-name") symbolName: String? = null,
    @Header("X-IBM-Notification-URL") notificationURL: String? = null,
    @Query("JESB") jesb: String? = null,
    @Body body: SubmitFileNameBody,
    @AvailableSince(ZVersion.ZOS_2_4)
    @Header("X-IBM-Intrdr-FileEncoding") fileEncoding: String? = null
  ): Call<SubmitJobRequest>

  @PUT("/zosmf/restjobs/jobs/{jobname}/{jobid}")
  fun holdJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("jobname") jobName: String,
    @Path("jobid") jobId: String,
    @Body body: HoldJobRequestBody
  ): Call<HoldJobRequest>

  @PUT("/zosmf/restjobs/jobs/{job-correlator}")
  fun holdJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Body body: HoldJobRequestBody
  ): Call<HoldJobRequest>

  @PUT("/zosmf/restjobs/jobs/{jobname}/{jobid}")
  fun releaseJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("jobname") jobName: String,
    @Path("jobid") jobId: String,
    @Body body: ReleaseJobRequestBody
  ): Call<ReleaseJobRequest>

  @PUT("/zosmf/restjobs/jobs/{job-correlator}")
  fun releaseJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Body body: ReleaseJobRequestBody
  ): Call<ReleaseJobRequest>

  @PUT("/zosmf/restjobs/jobs/{jobname}/{jobid}")
  fun cancelJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("jobname") jobName: String,
    @Path("jobid") jobId: String,
    @Body body: CancelJobRequestBody
  ): Call<CancelJobRequest>

  @PUT("/zosmf/restjobs/jobs/{job_correlator}")
  fun cancelJobRequest(
    @Header("Authorization") basicCredentials: String,
    @Path("job-correlator") jobCorrelator: String,
    @Body body: CancelJobRequestBody
  ): Call<CancelJobRequest>

  @DELETE("/zosmf/restjobs/jobs/{jobname}/{jobid}")
  fun cancelJobPurgeOutRequest(
    @Header("Authorization") basicCredentials: String,
    @Header("X-IBM-Job-Modify-Version") version : ProcessMethod = ProcessMethod.SYNCHRONOUS,
    @Path("jobname") jobName: String,
    @Path("jobid") jobId: String
  ): Call<CancelJobPurgeOutRequest>

  @DELETE("/zosmf/restjobs/jobs/{job_correlator}")
  fun cancelJobPurgeOutRequest(
    @Header("Authorization") basicCredentials: String,
    @Header("X-IBM-Job-Modify-Version") version : ProcessMethod? = null,
    @Path("job-correlator") jobCorrelator: String
  ): Call<CancelJobPurgeOutRequest>
}

enum class UseStepData(val value: String) {
  ENABLE("Y"),
  DISABLE("N");


  override fun toString(): String {
    return value
  }
}

enum class ContentType(val value: String) {
  TEXT_PLAIN("text/plain"),
  APP_STREAM("application/octet_stream"),
  APP_JSON("application/json");

  override fun toString(): String {
    return value
  }
}

enum class Intrdr_Recfm(val value: String) {
  F("F"),
  V("V");

  override fun toString(): String {
    return value
  }
}

enum class Intrdr_Mode(val value: String) {
  TEXT("TEXT"),
  RECORD("RECORD"),
  BINARY("BINARY");

  override fun toString(): String {
    return value
  }
}

enum class ExecData(val value: String) {
  YES("Y"),
  NO("N");


  override fun toString(): String {
    return value
  }
}

enum class ActiveStatus(val value:String) {
  ACTIVE("active"),
  DISABLE("disable");


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

enum class ProcessMethod(val value: String) {
  ASYNCHRONOUS("1.0"),
  SYNCHRONOUS("2.0");

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