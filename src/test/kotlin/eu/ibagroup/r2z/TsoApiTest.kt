// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import org.junit.jupiter.api.Test

class TsoApiTest: BaseTest() {

  private val tsoApi = buildGsonApi<TsoApi>(BASE_URL, UnsafeOkHttpClient.unsafeOkHttpClient)

  //You need to use the real servlet key
  //that was received in the body of the response to the startTso request
  private val servletKey = "MENTEE4-83-aabyaaad"

  @Test
  fun startTso() {
    val request = tsoApi.startTso(
      authorizationToken = BASIC_AUTH_TOKEN,
      proc = "IKJACCNT",
      chset = "697",
      cpage = TsoCodePage.IBM_1047,
      rows = 204,
      cols = 160,
      rsize = 50000,
      acct = "IZUACCT"
    )
    val response = request.execute()
    assert(response.code() == 200)
    val body = response.body() as TsoResponse
    assert(body.servletKey?.matches(Regex("${System.getenv("ZOSMF_TEST_USERNAME")}.*")) == true)
  }

  @Test
  fun sendMessageToTso() {
    val request = tsoApi.sendMessageToTso(
      authorizationToken = BASIC_AUTH_TOKEN,
      body = TsoData(
        tsoResponse = MessageType(version = "JSON", data = "DATA")
      ),
      servletKey = servletKey
    )
    val response = request.execute()
    assert(response.code() == 200)
    val body = response.body() as TsoResponse
    assert(body.servletKey == servletKey)
  }

  @Test
  fun receiveMessagesFromTso() {
    val request = tsoApi.receiveMessagesFromTso(
      authorizationToken = BASIC_AUTH_TOKEN,
      servletKey = servletKey
    )
    val response = request.execute()
    assert(response.code() == 200)
    val body = response.body() as TsoResponse
    assert(body.servletKey == servletKey)
  }

  @Test
  fun endTso() {
    val request = tsoApi.endTso(
      authorizationToken = BASIC_AUTH_TOKEN,
      servletKey = servletKey
    )
    val response = request.execute()
    assert(response.code() == 200)
    val body = response.body() as TsoResponse
    assert(body.servletKey == servletKey)
  }
}