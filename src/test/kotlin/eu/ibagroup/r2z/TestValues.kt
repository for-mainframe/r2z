package eu.ibagroup.r2z

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val zosmfUrl = System.getenv("ZOSMF_TEST_URL") ?: ""
val zosmfUser = System.getenv("ZOSMF_TEST_USERNAME") ?: ""
val zosmfPassword = System.getenv("ZOSMF_TEST_PASSWORD") ?: ""

val basicCreds = Credentials.basic(zosmfUser, zosmfPassword) ?: ""



fun errorBodyToList(errorBody: ResponseBody) : List<String> {
  return errorBody.charStream().readLines()
}