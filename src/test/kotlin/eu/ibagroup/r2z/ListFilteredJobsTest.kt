package eu.ibagroup.r2z

import org.junit.jupiter.api.*
import retrofit2.Call
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ListFilteredJobsTest : BaseTest() {

  var jobs: List<JobStatus> = emptyList()
  var jesApi: JESApi = buildApi(BASE_URL, getUnsafeOkHttpClient())


  val JOB_ID = "JOB00440"
  val USER_CORRELATOR = "MY_USER_CORRELATOR"


  @Test
  @Timeout(value = 3, unit = TimeUnit.SECONDS)
  fun getFilteredListByOwnerTest(){

    val call = jesApi.getFilteredJobs( BASIC_AUTH_TOKEN,"VKRUS")
    executeCallAndCheckResult(call){ job ->
      Assertions.assertEquals(job.owner, "VKRUS")
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