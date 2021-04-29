import com.google.gson.JsonObject
import eu.ibagroup.r2z.BaseTest
import eu.ibagroup.r2z.JESApi
import ibagroup.eu.r2z.HoldJobRequest
import ibagroup.eu.r2z.ReleaseJobRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HoldFor20sThenReleaseJobTest : BaseTest() {
    val JOB_ID = "JOB05571"
    val JOB_NAME = "NOTHINGJ"

    val JOB_CORRELATOR = "J0001561S0W1....D940967F.......:"

    // 0 - request was successful
    val SUCCESSFUL_REQUEST_RESULT = 0

    @Test
    fun holdFor20sThenReleaseJob() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()

        val request = retrofit.create(JESApi::class.java)
        val body = JsonObject()
        body.addProperty("request", "release")
        body.addProperty("version", "2.0")
        val firstCall: Call<HoldJobRequest> = request.holdJobRequest(BASIC_AUTH_TOKEN,
            JOB_NAME, JOB_ID, body)
        enqueueHoldCallAndCheckResult(firstCall)

        Thread.sleep(5000)

        val body2 = JsonObject()
        body2.addProperty("request", "hold")
        body2.addProperty("version", "2.0")
        val secondCall: Call<ReleaseJobRequest> = request.releaseJobRequest(BASIC_AUTH_TOKEN,
            JOB_NAME, JOB_ID, body2)

        enqueueReleaseCallAndCheckResult(secondCall)
    }

    fun enqueueHoldCallAndCheckResult(call: Call<HoldJobRequest>) {
        val response = call.execute()
        if (response.isSuccessful) {
            val jobStatus: HoldJobRequest = response.body() as HoldJobRequest
            println(jobStatus!!.status)
            Assertions.assertEquals(SUCCESSFUL_REQUEST_RESULT, jobStatus.status)
            Assertions.assertNotNull(jobStatus!!.owner)
            Assertions.assertEquals(jobStatus!!.owner?.toLowerCase(), "zosmfad")
        } else {
            println(response.errorBody())
            Assertions.assertTrue(false)
        }
    }

    fun enqueueReleaseCallAndCheckResult(call: Call<ReleaseJobRequest>) {
        val response = call.execute()
        if (response.isSuccessful) {
            val jobStatus: ReleaseJobRequest = response.body() as ReleaseJobRequest
            println(jobStatus!!.status)
            Assertions.assertEquals(SUCCESSFUL_REQUEST_RESULT, jobStatus.status)
            Assertions.assertNotNull(jobStatus!!.owner)
            Assertions.assertEquals(jobStatus!!.owner?.toLowerCase(), "zosmfad")
        } else {
            println(response.errorBody())
            Assertions.assertTrue(false)
        }
    }
}