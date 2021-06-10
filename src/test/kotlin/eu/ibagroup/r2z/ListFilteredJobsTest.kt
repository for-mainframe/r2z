// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import org.junit.jupiter.api.*
import retrofit2.Call
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ListFilteredJobsTest : BaseTest() {

  var jobs: List<JobStatus> = emptyList()
  var jesApi: JESApi = buildGsonApi(BASE_URL, getUnsafeOkHttpClient())


  val JOB_ID = "JOB05569"
  val USER_CORRELATOR = "J0006081S0W1....D9A8D97A.......:"


  @Test
  @Timeout(value = 3, unit = TimeUnit.SECONDS)
  fun getFilteredListByOwnerTest(){

    val call = jesApi.getFilteredJobs( BASIC_AUTH_TOKEN,"HLH")
    executeCallAndCheckResult(call){ job ->
      Assertions.assertEquals(job.owner, "HLH")
    }

  }

  @Test
  @Timeout(value = 3, unit = TimeUnit.SECONDS)
  fun getFilteredListByPrefixTest(){
    val call = jesApi.getFilteredJobs(BASIC_AUTH_TOKEN, prefix = "NOTH*")
    executeCallAndCheckResult(call){ job ->
      Assertions.assertTrue(Pattern.compile("(NOTH)+.*").matcher(job.jobName).matches())
    }
  }

  @Test
  @Timeout(value = 3, unit = TimeUnit.SECONDS)
  fun getFilteredListByJobIdTest(){
    val call = jesApi.getFilteredJobs(BASIC_AUTH_TOKEN, jobId = JOB_ID)
    executeCallAndCheckResult(call){ job ->
      Assertions.assertEquals(JOB_ID, job.jobId)
    }
  }

  @Test
  @Timeout(value = 3, unit = TimeUnit.SECONDS)
  fun getFilteredListByLimitCountTest(){
    val maxCount = 5
    val call = jesApi.getFilteredJobs(BASIC_AUTH_TOKEN, maxCount = maxCount)
    executeCallAndCheckResult(call, maxCount){}
  }

  @Test
  @Timeout(value = 3, unit = TimeUnit.SECONDS)
  fun getFilteredListByUserCorrelatorTest(){
    val call = jesApi.getFilteredJobs(BASIC_AUTH_TOKEN, userCorrelator = USER_CORRELATOR)
    executeCallAndCheckResult(call){ job->
      Assertions.assertEquals(job.jobCorrelator?.split(":")?.get(1), USER_CORRELATOR)

    }
  }

  fun executeCallAndCheckResult(call: Call<List<JobStatus>>, size: Int? = null, checkForAssertionFun: (job: JobStatus) -> Unit) {
    val response = call.execute()
    if (response.isSuccessful) {
      jobs = response.body() as List<JobStatus>
      if(size!=null) Assertions.assertEquals(jobs.size, size)
      jobs.forEach(checkForAssertionFun)


    } else {
      println(response.errorBody())
      Assertions.assertTrue(false)
    }

  }
}