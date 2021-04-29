package eu.ibagroup.r2z

import ibagroup.eu.r2z.CanJobPrgOutRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CancelJobAndPurgeOutputTest : BaseTest() {
    val JOB_ID = "JOB05571"
    val JOB_NAME = "NOTHINGJ"

    val JOB_CORRELATOR = "J0001561S0W1....D940967F.......:"

    // 0 - request was successful
    val SUCCESSFUL_REQUEST_RESULT = "0"

    @Test
    fun cancelJobAndPurgeOutput() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()

        val request = retrofit.create(JESApi::class.java)
        val firstCall: Call<CanJobPrgOutRequest> = request.
        canJobPrgOutRequest(BASIC_AUTH_TOKEN, "2.0", JOB_NAME, JOB_ID)
        enqueueHoldCallAndCheckResult(firstCall)
    }

    fun enqueueHoldCallAndCheckResult(call: Call<CanJobPrgOutRequest>) {
        val response = call.execute()

        if (response.isSuccessful)
        {
            val jobStatus: CanJobPrgOutRequest = response.body() as CanJobPrgOutRequest
            println(jobStatus!!.status)
            Assertions.assertEquals(SUCCESSFUL_REQUEST_RESULT, jobStatus.status)
            Assertions.assertNotNull(jobStatus!!.owner)
            Assertions.assertEquals(jobStatus!!.owner?.toLowerCase(), "zosmfad")
        } else
        {
            println(response.errorBody())
            Assertions.assertTrue(false)
        }
    }
}