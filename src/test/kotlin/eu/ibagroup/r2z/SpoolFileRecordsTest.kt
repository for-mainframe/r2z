// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Call


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpoolFileRecordsTest : BaseTest() {
  val jesApi = buildApi<JESApi>(BASE_URL, getUnsafeOkHttpClient())

  val JOB_CORRELATOR = "J0005569S0W1....D9975741.......:"
  val JOB_ID = "JOB05569"
  val JOB_NAME = "NOTHINGJ"

  @Test
  fun getRecordsInBinaryModeTest() {

    val call = jesApi.getSpoolFileRecords(BASIC_AUTH_TOKEN, JOB_CORRELATOR,2, BinaryMode.BINARY)
    executeCallAndCheckResult(call)
  }

  @Test
  fun getRecordsInTextModeTest() {
    val call = jesApi.getSpoolFileRecords(BASIC_AUTH_TOKEN, JOB_CORRELATOR, 2, BinaryMode.TEXT)
    executeCallAndCheckResult(call)
  }

  @Test
  fun getRecordsInRecordModeTest() {
    val call = jesApi.getSpoolFileRecords(BASIC_AUTH_TOKEN, JOB_NAME, JOB_ID, 2, BinaryMode.RECORD)
    executeCallAndCheckResult(call)
  }

  @Test
  fun getJCLRecordsInRangeTest() {
    val call = jesApi.getJCLRecords(BASIC_AUTH_TOKEN, JOB_CORRELATOR, BinaryMode.TEXT, RecordRange.withOffset(0, 5))
    executeCallAndCheckResult(call)
  }


  fun executeCallAndCheckResult(call: Call<ByteArray>) {
    val response = call.execute()
    if (response.isSuccessful == true) {
      var arr = response.body() as ByteArray
      println(arr.toString(Charsets.UTF_8))

    } else {
      Assertions.assertTrue(false)
    }

  }

}