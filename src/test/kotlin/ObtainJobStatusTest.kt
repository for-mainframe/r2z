import ibagroup.eu.r2z.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.locks.ReentrantLock

class ObtainJobStatusTest : BaseTest() {

  var jobStatus: JobStatus? = null
  var locker: ReentrantLock = ReentrantLock()
  var condition = locker.newCondition()


  val JOB_CORRELATOR = "J0000440S0W1....D92237DC.......:"
  val JOB_ID = "JOB00440"
  val JOB_NAME = "NOTHINGJ"

  @Test
  fun obtainStatusByNameAndIdTest() {
    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(getUnsafeOkHttpClient())
      .build()

    val request = retrofit.create(JESApi::class.java)
    val call: Call<JobStatus> = request.getJobStatus(BASIC_AUTH_TOKEN, JOB_NAME, JOB_ID, UseStepData.ENABLE)

    enqueueCallAndCheckResult(call)

  }

  @Test
  fun obtainStatusByCorrelator() {
    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(getUnsafeOkHttpClient())
      .build()

    val request = retrofit.create(JESApi::class.java)
    val call: Call<JobStatus> = request.getJobStatus(BASIC_AUTH_TOKEN, JOB_CORRELATOR)

    enqueueCallAndCheckResult(call)
  }


  fun enqueueCallAndCheckResult(call: Call<JobStatus>) {
    call.enqueue(object : Callback<JobStatus> {
      override fun onResponse(call: Call<JobStatus>, response: Response<JobStatus>) {
        locker.lock()

        if (response.isSuccessful) {
          jobStatus = response.body() as JobStatus
          println(jobStatus!!.status)
          jobStatus!!.steps.forEach { el->
            println(el)

          }

          Assertions.assertNotNull(jobStatus!!.owner)
          Assertions.assertEquals(jobStatus!!.owner?.toLowerCase(), "vkrus")

        } else {
          println(response.errorBody())
          Assertions.assertTrue(false)
        }

        condition.signalAll()
        locker.unlock()
      }

      override fun onFailure(call: Call<JobStatus>, t: Throwable) {
        locker.lock()

        t.printStackTrace()

        condition.signalAll()
        locker.unlock()
        Assertions.assertTrue(false)

      }

    })
    locker.lock()
    condition.await()
    locker.unlock()
  }


}
