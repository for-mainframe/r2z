// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import org.junit.jupiter.api.Test

class TsoApiTest: BaseTest() {

  private val tsoApi = buildGsonApi<TsoApi>(BASE_URL, UnsafeOkHttpClient.unsafeOkHttpClient)

  @Test
  fun startTso() {

    val request = tsoApi.startTso(
      authorizationToken = BASIC_AUTH_TOKEN,
      proc = "IKJACCNT",
      chset = "697",
      cpage = CodePage.IBM_1047,
      rows = 204,
      cols = 160,
      rsize = 50000,
      acct = "DEFAULT"
    )
    val response = request.execute()
    assert(response.code()== 200)
  }

  @Test
  fun sendMessageToTso() {
    val request = tsoApi.sendMessageToTso(
      authorizationToken = BASIC_AUTH_TOKEN,
      body = TsoData(
        messageType = MessageType(version = "JSON", dataType = "DATA")
      ),
      servletKey = "key"
    )
    val response = request.execute()
    assert(response.code()== 200)
  }

  @Test
  fun receiveMessagesFromTso() {
    val request = tsoApi.receiveMessagesFromTso(
      authorizationToken = BASIC_AUTH_TOKEN,
      servletKey = "key"
    )
    val response = request.execute()
    assert(response.code()== 200)
    val body = response.body() as TsoResponse
    assert(body.servletKey == "key")
  }

  @Test
  fun endTso() {
    val request = tsoApi.endTso(
      authorizationToken = BASIC_AUTH_TOKEN,
      servletKey = "key"
    )
    val response = request.execute()
    assert(response.code() == 200)
    val body = response.body() as TsoResponse
    assert(body.servletKey == "key")
  }
}