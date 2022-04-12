// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import eu.ibagroup.r2z.annotations.AvailableSince
import eu.ibagroup.r2z.annotations.ZVersion
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface DataAPI {

  @GET("zosmf/restfiles/ds")
  fun listDataSets(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-Attributes") xIBMAttr: XIBMAttr = XIBMAttr(),
    @Header("X-IBM-Max-Items") xIBMMaxItems: Int = 0,
    @Query("dslevel") dsLevel: String,
    @Query("volser") volser: String? = null,
    @Query("start") start: String? = null
  ): Call<DataSetsList>

  @GET("zosmf/restfiles/fs")
  fun listUssPath(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-Max-Items") xIBMMaxItems: Int = 0,
    @Header("X-IBM-Lstat") xIBMLstat: Boolean = false,
    @Query("path") path: String,
    @Query("depth") depth: Int = 1,
    @Query("filesys") fileSystem: String? = null,
    @Query("symlinks") followSymlinks: SymlinkMode? = null,
    @Query("group") group: String? = null,
    @Query("mtime") mtime: String? = null,
    @Query("name") name: String? = null,
    @Query("size") size: String? = null,
    @Query("perm") perm: String? = null,
    @Query("type") type: String? = null,
    @Query("user") user: String? = null
  ): Call<UssFilesList>

  @GET("zosmf/restfiles/ds/{dataset-name}/member")
  fun listDatasetMembers(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-Attributes") xIBMAttr: XIBMAttr = XIBMAttr(),
    @Header("X-IBM-Max-Items") xIBMMaxItems: Int = 0,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Path("dataset-name") datasetName: String,
    @Query("start") start: String? = null,
    @Query("pattern") pattern: String? = null
  ): Call<MembersList>


  @GET("/zosmf/restfiles/ds/{dataset-name}")
  fun retrieveDatasetContent(
    @Header("Authorization") authorizationToken: String,
    @Header("If-None-Match") ifNoneMatch: String? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("X-IBM-Return-Etag") xIBMReturnEtag: Boolean? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Header("X-IBM-Record-Range") xIBMRecordRange: XIBMRecordRange? = null,
    @Header("X-IBM-Obtain-ENQ") xIBMObtainENQ: XIBMObtainENQ? = null,
    @Header("X-IBM-Release-ENQ") xIBMReleaseENQ: Boolean? = null,
    @Header("X-IBM-Session-Ref") xIBMSessionRef: String? = null,
    @Path("dataset-name") datasetName: String,
    @Query("search") search: String? = null,
    @Query("research") research: String? = null,
    @Query("insensitive") insensitive: Boolean? = null,
    @Query("maxreturnsize") maxReturnSize: Int? = null
  ): Call<String>

  @GET("/zosmf/restfiles/ds/{dataset-name}({member-name})")
  fun retrieveMemberContent(
    @Header("Authorization") authorizationToken: String,
    @Header("If-None-Match") ifNoneMatch: String? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("X-IBM-Return-Etag") xIBMReturnEtag: Boolean? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Header("X-IBM-Record-Range") xIBMRecordRange: XIBMRecordRange? = null,
    @Header("X-IBM-Obtain-ENQ") xIBMObtainENQ: XIBMObtainENQ? = null,
    @Header("X-IBM-Release-ENQ") xIBMReleaseENQ: Boolean? = null,
    @Header("X-IBM-Session-Ref") xIBMSessionRef: String? = null,
    @Path("dataset-name") datasetName: String,
    @Path("member-name") memberName: String,
    @Query("search") search: String? = null,
    @Query("research") research: String? = null,
    @Query("insensitive") insensitive: Boolean? = null,
    @Query("maxreturnsize") maxReturnSize: Int? = null
  ): Call<String>

  @GET("/zosmf/restfiles/ds/-({volser})/{dataset-name}")
  fun retrieveDatasetContent(
    @Header("Authorization") authorizationToken: String,
    @Header("If-None-Match") ifNoneMatch: String? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("X-IBM-Return-Etag") xIBMReturnEtag: Boolean? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Header("X-IBM-Record-Range") xIBMRecordRange: XIBMRecordRange? = null,
    @Header("X-IBM-Obtain-ENQ") xIBMObtainENQ: XIBMObtainENQ? = null,
    @Header("X-IBM-Release-ENQ") xIBMReleaseENQ: Boolean? = null,
    @Header("X-IBM-Session-Ref") xIBMSessionRef: String? = null,
    @Path("volser") volser: String,
    @Path("dataset-name") datasetName: String,
    @Query("search") search: String? = null,
    @Query("research") research: String? = null,
    @Query("insensitive") insensitive: Boolean? = null,
    @Query("maxreturnsize") maxReturnSize: Int? = null
  ): Call<String>

  @GET("/zosmf/restfiles/ds/-({volser})/{dataset-name}({member-name})")
  fun retrieveMemberContent(
    @Header("Authorization") authorizationToken: String,
    @Header("If-None-Match") ifNoneMatch: String? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("X-IBM-Return-Etag") xIBMReturnEtag: Boolean? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Header("X-IBM-Record-Range") xIBMRecordRange: XIBMRecordRange? = null,
    @Header("X-IBM-Obtain-ENQ") xIBMObtainENQ: XIBMObtainENQ? = null,
    @Header("X-IBM-Release-ENQ") xIBMReleaseENQ: Boolean? = null,
    @Header("X-IBM-Session-Ref") xIBMSessionRef: String? = null,
    @Path("volser") volser: String,
    @Path("dataset-name") datasetName: String,
    @Path("member-name") memberName: String,
    @Query("search") search: String? = null,
    @Query("research") research: String? = null,
    @Query("insensitive") insensitive: Boolean? = null,
    @Query("maxreturnsize") maxReturnSize: Int? = null
  ): Call<String>


  @PUT("/zosmf/restfiles/ds/{dataset-name}")
  fun writeToDataset(
    @Header("Authorization") authorizationToken: String,
    @Header("If-Match") ifMatch: String? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Header("X-IBM-Obtain-ENQ") xIBMObtainENQ: XIBMObtainENQ? = null,
    @Header("X-IBM-Release-ENQ") xIBMReleaseENQ: Boolean? = null,
    @Header("X-IBM-Session-Ref") xIBMSessionRef: String? = null,
    @Body content: String,
    @Path("dataset-name") datasetName: String
  ): Call<Void>

  @PUT("/zosmf/restfiles/ds/{dataset-name}({member-name})")
  fun writeToDatasetMember(
    @Header("Authorization") authorizationToken: String,
    @Header("If-Match") ifMatch: String? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Header("X-IBM-Obtain-ENQ") xIBMObtainENQ: XIBMObtainENQ? = null,
    @Header("X-IBM-Release-ENQ") xIBMReleaseENQ: Boolean? = null,
    @Header("X-IBM-Session-Ref") xIBMSessionRef: String? = null,
    @Body content: String,
    @Path("dataset-name") datasetName: String,
    @Path("member-name") memberName: String
  ): Call<Void>

  @PUT("/zosmf/restfiles/ds/-({volser})/{dataset-name}")
  fun writeToDataset(
    @Header("Authorization") authorizationToken: String,
    @Header("If-Match") ifMatch: String? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Header("X-IBM-Obtain-ENQ") xIBMObtainENQ: XIBMObtainENQ? = null,
    @Header("X-IBM-Release-ENQ") xIBMReleaseENQ: Boolean? = null,
    @Header("X-IBM-Session-Ref") xIBMSessionRef: String? = null,
    @Body content: String,
    @Path("volser") volser: String,
    @Path("dataset-name") datasetName: String
  ): Call<Void>

  @PUT("/zosmf/restfiles/ds/-({volser})/{dataset-name}({member-name})")
  fun writeToDatasetMember(
    @Header("Authorization") authorizationToken: String,
    @Header("If-Match") ifMatch: String? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Header("X-IBM-Obtain-ENQ") xIBMObtainENQ: XIBMObtainENQ? = null,
    @Header("X-IBM-Release-ENQ") xIBMReleaseENQ: Boolean? = null,
    @Header("X-IBM-Session-Ref") xIBMSessionRef: String? = null,
    @Body content: String,
    @Path("volser") volser: String,
    @Path("dataset-name") datasetName: String,
    @Path("member-name") memberName: String
  ): Call<Void>

  @POST("/zosmf/restfiles/ds/{dataset-name}")
  fun createDataset(
    @Header("Authorization") authorizationToken: String,
    @Path("dataset-name") datasetName: String,
    @Body body: CreateDataset
  ): Call<Void>

  @DELETE("/zosmf/restfiles/ds/{dataset-name}")
  fun deleteDataset(
    @Header("Authorization") authorizationToken: String,
    @Path("dataset-name") datasetName: String,
  ): Call<Void>

  @DELETE("/zosmf/restfiles/ds/-({volume})/{dataset-name}")
  fun deleteDataset(
    @Header("Authorization") authorizationToken: String,
    @Path("volume") volume: String,
    @Path("dataset-name") datasetName: String,
  ): Call<Void>

  @DELETE("/zosmf/restfiles/ds/{dataset-name}({member-name})")
  fun deleteDatasetMember(
    @Header("Authorization") authorizationToken: String,
    @Path("dataset-name") datasetName: String,
    @Path("member-name") memberName: String,
  ): Call<Void>

  @DELETE("/zosmf/restfiles/ds/-({volume})/{dataset-name}({member-name})")
  fun deleteDatasetMember(
    @Header("Authorization") authorizationToken: String,
    @Path("volume") volume: String,
    @Path("dataset-name") datasetName: String,
    @Path("member-name") memberName: String,
  ): Call<Void>

  @PUT("/zosmf/restfiles/ds/{to-data-set-name}")
  fun renameDataset(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Body body: RenameData,
    @Path("to-data-set-name") toDatasetName: String
  ): Call<Void>

  @PUT("/zosmf/restfiles/ds/{to-data-set-name}({member-name})")
  fun renameDatasetMember(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Body body: RenameData,
    @Path("to-data-set-name") toDatasetName: String,
    @Path("member-name") memberName: String
  ): Call<Void>

  /**
   * Copy from - to
   * SEQ -> SEQ
   * PDS MEMBER -> SEQ (overwrites content)
   * PDS MEMBER or MEMBERS -> PDS (adds or replaces)
   */
  @PUT("/zosmf/restfiles/ds/{to-data-set-name}")
  fun copyToDataset(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Body body: CopyDataZOS.CopyFromDataset,
    @Path("to-data-set-name") toDatasetName: String
  ): Call<Void>

  /**
   * Volser for uncatalogued datasets
   * Copy from - to
   * SEQ -> SEQ
   * PDS MEMBER -> SEQ
   * PDS MEMBER or MEMBERS -> PDS
   */
  @PUT("/zosmf/restfiles/ds/-({to-volser})/{to-data-set-name}")
  fun copyToDataset(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Body body: CopyDataZOS.CopyFromDataset,
    @Path("to-volser") toVolser: String,
    @Path("to-data-set-name") toDatasetName: String
  ): Call<Void>

  /**
   * SEQ -> PDS MEMBER
   * PDS MEMBER -> PDS MEMBER
   */
  @PUT("/zosmf/restfiles/ds/{to-data-set-name}({member-name})")
  fun copyToDatasetMember(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Body body: CopyDataZOS.CopyFromDataset,
    @Path("to-data-set-name") toDatasetName: String,
    @Path("member-name") memberName: String
  ): Call<Void>

  /**
   * Volser for uncatalogued datasets
   * SEQ -> PDS MEMBER
   * PDS MEMBER -> PDS MEMBER
   */
  @PUT("/zosmf/restfiles/ds/-({to-volser})/{to-data-set-name}({member-name})")
  fun copyToDatasetMemberFromUssFile(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Body body: CopyDataZOS.CopyFromDataset,
    @Path("to-volser") toVolser: String,
    @Path("to-data-set-name") toDatasetName: String,
    @Path("member-name") memberName: String
  ): Call<Void>

  /**
   * USS FILE -> SEQ (truncates contents)
   */
  @PUT("/zosmf/restfiles/ds/{to-data-set-name}")
  fun copyToDatasetFromUss(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Body body: CopyDataZOS.CopyFromFile,
    @Path("to-data-set-name") toDatasetName: String
  ): Call<Void>

  /**
   * USS FILE -> PDS MEMBER
   */
  @PUT("/zosmf/restfiles/ds/{to-data-set-name}({member-name})")
  fun copyToDatasetMemberFromUssFile(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Header("X-IBM-Migrated-Recall") xIBMMigratedRecall: MigratedRecall? = null,
    @Body body: CopyDataZOS.CopyFromFile,
    @Path("to-data-set-name") toDatasetName: String,
    @Path("member-name") memberName: String
  ): Call<Void>


  @PUT("/zosmf/restfiles/ds/{dataset-name}")
  fun recallMigratedDataset(
    @Header("Authorization") authorizationToken: String,
    @Body body: HRecall = HRecall(),
    @Path("dataset-name") datasetName: String
  ): Call<Void>

  @PUT("/zosmf/restfiles/ds/{dataset-name}")
  fun migrateDataset(
    @Header("Authorization") authorizationToken: String,
    @Body body: HMigrate = HMigrate(),
    @Path("dataset-name") datasetName: String
  ): Call<Void>

  @PUT("/zosmf/restfiles/ds/{dataset-name}")
  fun deleteMigratedDataset(
    @Header("Authorization") authorizationToken: String,
    @Body body: HDelete = HDelete(),
    @Path("dataset-name") datasetName: String
  ): Call<Void>


  @GET("/zosmf/restfiles/fs/{filepath-name}")
  fun retrieveUssFileContent(
    @Header("Authorization") authorizationToken: String,
    @Header("If-None-Match") ifNoneMatch: String? = null,
    @Header("Range") range: Int? = null,
    @Header("X-IBM-Record-Range") xIBMRecordRange: XIBMRecordRange? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("Accept-Encoding") acceptEncoding: String? = "gzip",
    @Path("filepath-name") filePath: String,
    @Query("search") search: String? = null,
    @Query("research") research: String? = null,
    @Query("insensitive") insensitive: Boolean? = null,
    @Query("maxreturnsize") maxReturnSize: Int? = null
  ): Call<ResponseBody>

  @PUT("/zosmf/restfiles/fs/{filepath-name}")
  fun writeToUssFile(
    @Header("Authorization") authorizationToken: String,
    @Header("If-Match") ifNoneMatch: String? = null,
    @Header("X-IBM-Data-Type") xIBMDataType: XIBMDataType? = null,
    @Header("Accept-Encoding") acceptEncoding: String? = "gzip",
    @Header("Content-Type") contentType: String? = null,
    @Path("filepath-name") filePath: String,
    @Body body: String
  ): Call<Void>

  @POST("/zosmf/restfiles/fs/{filepath-name}")
  fun createUssFile(
    @Header("Authorization") authorizationToken: String,
    @Path("filepath-name") filePath: FilePath,
    @Body body: CreateUssFile
  ): Call<Void>

  @DELETE("/zosmf/restfiles/fs/{filepath-name}")
  fun deleteUssFile(
    @Header("Authorization") authorizationToken: String,
    @Path("filepath-name") filePath: String,
    @Header("X-IBM-Option") xIBMOption: XIBMOption? = null
  ): Call<Void>

  @PUT("/zosmf/restfiles/fs/{filepath-name}")
  fun changeFileMode(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Body body: ChangeMode,
    @Path("filepath-name") filePath: FilePath,
  ): Call<Void>

  @PUT("/zosmf/restfiles/fs/{filepath-name}")
  fun changeFileOwner(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Body body: ChangeOwner,
    @Path("filepath-name") filePath: FilePath,
  ): Call<Void>

  @PUT("/zosmf/restfiles/fs/{filepath-name}")
  fun changeFileTag(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Body body: ChangeOwner,
    @Path("filepath-name") filePath: FilePath,
  ): Call<Void>

  @PUT("/zosmf/restfiles/fs/{filepath-name}")
  fun moveUssFile(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Body body: MoveUssFile,
    @Path("filepath-name") filePath: FilePath,
  ): Call<Void>

  @PUT("/zosmf/restfiles/fs/{filepath-name}")
  fun copyUssFile(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Body body: CopyDataUSS.CopyFromFileOrDir,
    @Path("filepath-name") filePath: FilePath,
  ): Call<Void>

  /**
   * SEQ -> USS FILE
   * PDS MEMBER -> USS FILE
   * PDS -> USS DIR doesn't work
   */
  @PUT("/zosmf/restfiles/fs/{filepath-name}")
  fun copyDatasetOrMemberToUss(
    @Header("Authorization") authorizationToken: String,
    @Header("X-IBM-BPXK-AUTOCVT") xIBMBpxkAutoCvt: XIBMBpxkAutoCvt? = null,
    @Body body: CopyDataUSS.CopyFromDataset,
    @Path("filepath-name") filePath: FilePath,
  ): Call<Void>

}


data class FilePath(private val path: String) {
  override fun toString(): String {
    return if (path.startsWith('/')) path.substring(1) else path
  }
}

enum class XIBMOption(private val type: String = "recursive") {

  RECURSIVE("recursive");

  override fun toString(): String {
    return type
  }


}


enum class XIBMObtainENQ(private val type: String) {

  EXCL("excl"),
  SHRW("shrw");

  override fun toString(): String {
    return type
  }


}

enum class XIBMBpxkAutoCvt(private val type: String) {
  ON("on"),
  ALL("all"),
  OFF("off");

  override fun toString(): String {
    return type
  }


}

data class XIBMRecordRange(private val format: Format, private val sss: Int, private val nnn: Int) {

  enum class Format {
    DASHED,
    COMA_SEPARATED
  }

  override fun toString(): String {
    return when (format) {
      Format.DASHED -> "$sss-$nnn"
      Format.COMA_SEPARATED -> "$sss,$nnn"
    }
  }


}

private const val codePagePrefix = "IBM-"

enum class CodePage(val codePage: String) {
  IBM_1025("${codePagePrefix}1025"),
  IBM_1047("${codePagePrefix}1047")
}

data class XIBMDataType(
  val type: Type,
  @AvailableSince(ZVersion.ZOS_2_4) val encoding: CodePage? = null
) {

  enum class Type(val value: String) {
    TEXT("text"),
    BINARY("binary"),
    RECORD("record")
  }


  override fun toString(): String {
    return if (encoding != null) "${type.value};fileEncoding=${encoding.codePage}" else type.value
  }


}

data class XIBMAttr(private val type: Type = Type.BASE, private val isTotal: Boolean = false) {

  enum class Type(val queryVal: String) {
    BASE("base"),
    VOL("vol"),
    DSNAME("dsname"),
    MEMBER("member")
  }

  override fun toString(): String {
    val suffix = if (isTotal) ",total" else ""
    return type.queryVal + suffix
  }

}

enum class SymlinkMode(private val symlinksVal: String) {
  FOLLOW("follow"),
  REPORT("report");

  override fun toString(): String {
    return symlinksVal
  }


}

enum class MigratedRecall(private val recallMode: String) {

  WAIT("wait"),
  NOWAIT("nowait"),
  ERROR("error");

  override fun toString(): String {
    return recallMode
  }


}
