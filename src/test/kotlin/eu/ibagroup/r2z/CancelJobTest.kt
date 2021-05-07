package eu.ibagroup.r2z

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CancelJobTest : BaseTest() {
    val JOB_ID = "JOB06200"
    val JOB_NAME = "NOTHINGJ"

    val JOB_CORRELATOR = "J0001561S0W1....D940967F.......:"

    // 0 - request was successful
    val SUCCESSFUL_REQUEST_RESULT = "0"

    @Test
    fun cancelJobTest() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()

        val request = retrofit.create(JESApi::class.java)
        val call: Call<CancelJobRequest> = request.cancelJobRequest(BASIC_AUTH_TOKEN, JOB_NAME,
            JOB_ID, CancelJobRequestBody())
        enqueueCancelJobCallAndCheckResult(call)
    }

    fun enqueueCancelJobCallAndCheckResult(call: Call<CancelJobRequest>) {
        val response = call.execute()

        if (response.isSuccessful)
        {
            val jobStatus: CancelJobRequest = response.body() as CancelJobRequest
            println(jobStatus!!.status)
            Assertions.assertEquals(SUCCESSFUL_REQUEST_RESULT, jobStatus.status)
            Assertions.assertNotNull(jobStatus!!.owner)
            Assertions.assertEquals(jobStatus!!.owner?.toLowerCase(), "hlh")
        } else
        {
            println(response.errorBody())
            Assertions.assertTrue(false)
        }
    }
}