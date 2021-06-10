// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Call

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ObtainJobStatusTest : BaseTest() {

  var jesApi: JESApi = buildGsonApi(BASE_URL, getUnsafeOkHttpClient())

  val JOB_CORRELATOR = "J0006081S0W1....D9A8D97A.......:"
  val JOB_ID = "JOB05569"
  val JOB_NAME = "NOTHINGJ"



  @Test
  fun obtainStatusByNameAndIdTest() {
    val call = jesApi.getJobStatus(BASIC_AUTH_TOKEN, JOB_NAME, JOB_ID, UseStepData.DISABLE)
    executeCallAndCheckResult(call)
  }

  @Test
  fun obtainStatusByCorrelator() {
    val call = jesApi.getJobStatus(BASIC_AUTH_TOKEN, JOB_CORRELATOR)
    executeCallAndCheckResult(call)
  }


  fun executeCallAndCheckResult(call: Call<JobStatus>) {
    val response = call.execute()
    if (response.isSuccessful) {
      val jobStatus = response.body() as JobStatus
      jobStatus.steps?.forEach { el ->
        println(el)

      }

      Assertions.assertNotNull(jobStatus.owner)
      Assertions.assertEquals(jobStatus.owner.toLowerCase(), "hlh")

    } else {
      println(response.errorBody())
      Assertions.assertTrue(false)
    }

  }


}
