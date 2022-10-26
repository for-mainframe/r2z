// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosjobs

import eu.ibagroup.r2z.*
import eu.ibagroup.r2z.zowe.client.sdk.core.ZOSConnection
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.SubmitJclParams
import eu.ibagroup.r2z.zowe.client.sdk.zosjobs.input.SubmitJobParams
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response

/**
 * Class to handle submitting of z/OS batch jobs via z/OSMF
 */
class SubmitJobs(
  var connection: ZOSConnection,
  var httpClient: OkHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient
) {

  init {
    connection.checkConnection()
  }

  var response: Response<*>? = null

  /**
   * Submit a job that resides in a z/OS data set.
   *
   * @param jobDataSet job Dataset to be translated into SubmitJobParams object
   * @return job document with details about the submitted job
   * @throws Exception error on submitting
   */
  fun submitJob(jobDataSet: String): SubmitJobRequest {
    return this.submitJobCommon(SubmitJobParams(jobDataSet))
  }

  /**
   * Submit a job that resides in a z/OS data set.
   *
   * @param params submit job parameters, see SubmitJobParams object
   * @return job document with details about the submitted job
   * @throws Exception error on submitting
   */
  fun submitJobCommon(params: SubmitJobParams): SubmitJobRequest {
    val baseUrl = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApi<JESApi>(baseUrl, httpClient)
    val call = jesApi.submitJobRequest(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      body = SubmitFileNameBody(params.jobDataSet),
      symbolName = params.jclSymbols
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as SubmitJobRequest? ?: throw Exception("No body returned")
  }

  /**
   * Submit a string of JCL to run
   *
   * @param jcl JCL content that you want to be submitted
   * @param internalReaderRecfm record format of the jcl you want to submit. "F" (fixed) or "V" (variable)
   * @param internalReaderLrecl logical record length of the jcl you want to submit
   * @return job document with details about the submitted job
   * @throws Exception error on submitting
   */
  fun submitJcl(jcl: String, internalReaderRecfm: Intrdr_Recfm, internalReaderLrecl: String): SubmitJobRequest {
    return submitJclCommon(SubmitJclParams(jcl, internalReaderRecfm, internalReaderLrecl))
  }

  /**
   * Submit a JCL string to run
   *
   * @param params submit jcl parameters, see SubmitJclParams object
   * @return job document with details about the submitted job
   * @throws Exception error on submitting
   */
  fun submitJclCommon(params: SubmitJclParams): SubmitJobRequest {
    val url = "${connection.protocol}://${connection.host}:${connection.zosmfPort}"
    val jesApi = buildApi<JESApi>(url, httpClient)
    val call = jesApi.submitJobRequest(
      basicCredentials = Credentials.basic(connection.user, connection.password),
      body = SubmitFileNameBody(params.jcl),
      recfm = params.internalReaderRecfm,
      lrecl = params.internalReaderLrecl,
      symbolName = params.jclSymbols
    )
    response = call.execute()
    if (response?.isSuccessful != true) {
      throw Exception(response?.errorBody()?.string())
    }
    return response?.body() as SubmitJobRequest? ?: throw Exception("No body returned")
  }

}