package eu.ibagroup.r2z.zowe

import eu.ibagroup.r2z.zowe.config.decodeFromBase64
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.Assertions

class MockResponseDispatcher : Dispatcher() {

  var validationList = mutableListOf<Pair<(RecordedRequest?)->Boolean, (RecordedRequest?)->MockResponse>>()
  private var endpointsWithoutAuthorization = mutableSetOf<String>()

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

  fun removeAuthorizationFromZosmfEndpoint (endpoint: String) {
    endpointsWithoutAuthorization.add(endpoint)
  }

  fun injectEndpoint (acceptable: (RecordedRequest?)->Boolean, handler: (RecordedRequest?)->MockResponse) {
    validationList.add(Pair(acceptable, handler))
  }

  fun clearValidationList () {
    validationList.clear()
  }

  override fun dispatch(request: RecordedRequest): MockResponse {
    val zosmfPath = request.requestLine.split("/zosmf/").last()
    if (endpointsWithoutAuthorization.none { zosmfPath.contains(it) }) {
      val authTokenRequest = request.getHeader("Authorization") ?: Assertions.fail("auth token must be presented.")
      val credentials = decodeBasicAuthToken(authTokenRequest).split(":")
      val usernameRequest = credentials[0]
      val passwordRequest = credentials[1]
      Assertions.assertEquals(usernameRequest, TEST_USER)
      Assertions.assertEquals(passwordRequest, TEST_PASSWORD)
    }

    return validationList.firstOrNull { it.first(request) }?.second?.let { it(request) } ?: return MockResponse().setResponseCode(404)
  }
}
