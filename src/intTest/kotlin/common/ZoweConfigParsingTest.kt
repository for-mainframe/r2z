// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package common

import okhttp3.mockwebserver.MockWebServer
import eu.ibagroup.r2z.DataAPI
import eu.ibagroup.r2z.zowe.config.*
import okhttp3.OkHttpClient
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZoweConfigParsingTest() {

  lateinit var mockServer: MockWebServer
  lateinit var proxyClient: OkHttpClient
  lateinit var keytarWrapper: KeytarWrapper
  lateinit var zoweConfig: ZoweConfig

  @Test
  fun readConfigAndListDatasets() {
    val authToken = zoweConfig.getAuthEncoding().withBasicPrefix()

    val dataApi = buildGsonApi<DataAPI>("http://${zoweConfig.host}:${zoweConfig.port}", proxyClient)
    val response = dataApi
      .listDataSets(
        authorizationToken = authToken,
        dsLevel = "TEST.*"
      )
      .execute()
    if (response.isSuccessful) {
      val datasetLists = response.body()
      Assertions.assertEquals(datasetLists?.items?.size, 4)
    } else {
      Assertions.fail("response must be successful.")
    }
  }

}
