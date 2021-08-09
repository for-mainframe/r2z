// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Call

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ListSpoolFilesTest : BaseTest(){

  var jesApi: JESApi = buildGsonApi(BASE_URL, getUnsafeOkHttpClient())

  val JOB_CORRELATOR = "J0005569S0W1....D9975741.......:"
  val JOB_ID = "JOB05569"
  val JOB_NAME = "NOTHINGJ"


  @Test
  fun getListSpoolFilesByCorrelator(){
    val call = jesApi.getJobSpoolFiles(BASIC_AUTH_TOKEN, JOB_CORRELATOR)
    executeCallAndCheckResult(call)
  }

  @Test
  fun getListSpoolFilesByIdAndName(){
    val call = jesApi.getJobSpoolFiles(BASIC_AUTH_TOKEN, JOB_NAME, JOB_ID)
    executeCallAndCheckResult(call)
  }

  fun executeCallAndCheckResult(call: Call<List<SpoolFile>>){
    var response = call.execute()
    if(response.isSuccessful == true){
      var spoolFiles = response.body()
      spoolFiles?.forEach { el-> println(el) }
    } else{
      Assertions.assertTrue(false)
    }
  }
}