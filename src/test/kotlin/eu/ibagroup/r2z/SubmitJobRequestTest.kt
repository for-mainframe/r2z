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
import retrofit2.converter.scalars.ScalarsConverterFactory

class SubmitJobRequestTest : BaseTest() {
    val JOB_PATH = "//'HHAL.PLUGIN.TEST.JOBS(JOB1)'"

    @Test
    fun submitJobOnZOS_System() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()

        val request = retrofit.create(JESApi::class.java)
        val call: Call<SubmitJobRequest> = request.submitJobRequest(BASIC_AUTH_TOKEN,
            body = SubmitFileNameBody(file = JOB_PATH))
        enqueueSubmitJob(call)
    }

    @Test
    fun submitJobFromInputText() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()

        val request = retrofit.create(JESApi::class.java)
        val body = "" +
                "//NOTHINGJ    JOB CLASS=B,MSGLEVEL=(1,1),MSGCLASS=X,\n" +
                "//            NOTIFY=HAL,REGION=6M\n" +
                "//*********\n" +
                "//STEP0       EXEC PGM=IEFBR14\n" +
                "//SYSPRINT    DD SYSOUT=*\n" +
                "//SYSIN       DD *\n" +
                "//STEP1       EXEC PGM=IKJEFT01,PARM='JOB2'\n" +
                "//SYSPROC     DD   DSN=HHAL.PLUGIN.TEST.JOBS,DISP=SHR\n" +
                "//SYSTSPRT    DD   SYSOUT=*\n" +
                "//SYSTSIN     DD   DUMMY,DCB=BLKSIZE=80"
        val call: Call<SubmitJobRequest> = request.submitJobRequest(BASIC_AUTH_TOKEN, body = body)
        enqueueSubmitJob(call)
    }

    fun enqueueSubmitJob(call: Call<SubmitJobRequest>) {
        val response = call.execute()
        if (response.isSuccessful) {
            val jobStatus: SubmitJobRequest = response.body() as SubmitJobRequest
            println(jobStatus!!.jobid)
            println(jobStatus!!.jobname)
            println(jobStatus!!.status)
            Assertions.assertNotNull(jobStatus!!.owner)
            Assertions.assertEquals(jobStatus!!.owner?.toLowerCase(), "hlh")
        } else {
            println(response.errorBody())
            Assertions.assertTrue(false)
        }
    }
}