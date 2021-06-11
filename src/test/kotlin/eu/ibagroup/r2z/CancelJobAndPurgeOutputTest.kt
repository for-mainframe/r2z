/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright © 2021 IBA Group, a.s.
 */

package eu.ibagroup.r2z

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CancelJobAndPurgeOutputTest : BaseTest() {
    val JOB_ID = "JOB06199"
    val JOB_NAME = "NOTHINGJ"

    val JOB_CORRELATOR = "J0001561S0W1....D940967F.......:"

    // 0 - request was successful
    val SUCCESSFUL_REQUEST_RESULT = "0"

    @Test
    fun cancelJobAndPurgeOutputTest() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()

        val request = retrofit.create(JESApi::class.java)
        val call: Call<CancelJobPurgeOutRequest> = request.
            cancelJobPurgeOutRequest(BASIC_AUTH_TOKEN,
            version = ProcessMethod.SYNCHRONOUS,
            jobName = JOB_NAME,
            jobId = JOB_ID)
        enqueueCancelCallAndCheckResult(call)
    }

    fun enqueueCancelCallAndCheckResult(call: Call<CancelJobPurgeOutRequest>) {
        val response = call.execute()

        if (response.isSuccessful)
        {
            val jobStatus: CancelJobPurgeOutRequest = response.body() as CancelJobPurgeOutRequest
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