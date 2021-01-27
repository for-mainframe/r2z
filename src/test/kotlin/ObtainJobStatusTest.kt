import ibagroup.eu.r2z.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ObtainJobStatusTest : BaseTest() {

  var jesApi: JESApi = buildGsonApi(BASE_URL, getUnsafeOkHttpClient())

  val JOB_CORRELATOR = "J0000440S0W1....D92237DC.......:"
  val JOB_ID = "JOB00440"
  val JOB_NAME = "NOTHINGJ"



  @Test
  fun obtainStatusByNameAndIdTest() {
    val call = jesApi.getJobStatus(BASIC_AUTH_TOKEN, JOB_NAME, JOB_ID, UseStepData.ENABLE)
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
      Assertions.assertEquals(jobStatus.owner.toLowerCase(), "vkrus")

    } else {
      println(response.errorBody())
      Assertions.assertTrue(false)
    }

  }


}
