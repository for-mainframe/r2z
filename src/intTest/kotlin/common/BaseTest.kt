// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package common

import eu.ibagroup.r2z.BytesConverterFactory
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

inline fun <reified Api> buildGsonApi(baseUrl: String, client: OkHttpClient) = Retrofit.Builder()
  .baseUrl(baseUrl)
  .addConverterFactory(GsonConverterFactory.create())
  .client(client)
  .build()
  .create(Api::class.java)

inline fun <reified Api> buildApi(baseUrl: String, client: OkHttpClient) = Retrofit.Builder()
  .baseUrl(baseUrl)
  .addConverterFactory(BytesConverterFactory.create())
  .client(client)
  .build()
  .create(Api::class.java)

open class BaseTest {
  val BASE_URL = System.getenv("ZOSMF_TEST_URL")
  val BASIC_AUTH_TOKEN = Credentials.basic(
    System.getenv("ZOSMF_TEST_USERNAME"),
    System.getenv("ZOSMF_TEST_PASSWORD")
  )


  fun getUnsafeOkHttpClient(): OkHttpClient {
    return try {


      val trustAllCerts: Array<TrustManager> = arrayOf(
        object : X509TrustManager {
          @Throws(CertificateException::class)
          override fun checkClientTrusted(
            chain: Array<X509Certificate?>?,
            authType: String?
          ) {
          }

          @Throws(CertificateException::class)
          override fun checkServerTrusted(
            chain: Array<X509Certificate?>?,
            authType: String?
          ) {
          }

          override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
          }
        }
      )
      val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
      sslContext.init(null, trustAllCerts, SecureRandom())

      val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
      val builder = OkHttpClient.Builder()
      builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
      builder.hostnameVerifier(object : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
          return true
        }
      })
      builder.build()
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }
}
