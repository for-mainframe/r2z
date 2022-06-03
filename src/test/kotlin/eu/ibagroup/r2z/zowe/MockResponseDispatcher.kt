package eu.ibagroup.r2z.zowe

import com.squareup.okhttp.mockwebserver.Dispatcher
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.RecordedRequest
import eu.ibagroup.r2z.zowe.config.decodeFromBase64
import org.junit.jupiter.api.Assertions

class MockResponseDispatcher : Dispatcher() {

  var validationList = mutableListOf<Pair<(RecordedRequest?)->Boolean, (RecordedRequest?)->MockResponse>>()

  private fun getResourceText(resourcePath: String): String? {
    return javaClass.classLoader.getResource(resourcePath)?.readText()
  }

  fun readMockJson(mockFilePath: String): String? {
    return getResourceText("mock/${mockFilePath}.json")
  }

  fun decodeBasicAuthToken (token: String): String {
    val base64Credentials: String = token.substring("Basic".length).trim()
    return base64Credentials.decodeFromBase64()
  }

  fun injectEndpoint (acceptable: (RecordedRequest?)->Boolean, handler: (RecordedRequest?)->MockResponse) {
    validationList.add(Pair(acceptable, handler))
  }

  fun clearValidationList () {
    validationList.clear()
  }

  override fun dispatch(request: RecordedRequest?): MockResponse {
    val authTokenRequest = request?.getHeader("Authorization") ?: Assertions.fail("auth token must be presented.")
    val credentials = decodeBasicAuthToken(authTokenRequest).split(":")
    val usernameRequest = credentials[0]
    val passwordRequest = credentials[1]
    Assertions.assertEquals(usernameRequest, TEST_USER)
    Assertions.assertEquals(passwordRequest, TEST_PASSWORD)

    return validationList.firstOrNull { it.first(request) }?.second?.let { it(request) } ?: return MockResponse().setResponseCode(404)
  }
}
