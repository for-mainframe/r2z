// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import okhttp3.OkHttpClient
import java.lang.Exception
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.Throws
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession
import java.lang.RuntimeException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.time.Duration

object UnsafeOkHttpClient {
  // Create a trust manager that does not validate certificate chains
  val unsafeOkHttpClient: OkHttpClient

  // Install the all-trusting trust manager

    // Create an ssl socket factory with our all-trusting manager
    get() = try {
      // Create a trust manager that does not validate certificate chains
      val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
          @Throws(CertificateException::class)
          override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
          }

          @Throws(CertificateException::class)
          override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
          }

          override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
          }
        }
      )

      // Install the all-trusting trust manager
      val sslContext = SSLContext.getInstance("SSL")
      sslContext.init(null, trustAllCerts, SecureRandom())

      // Create an ssl socket factory with our all-trusting manager
      val sslSocketFactory = sslContext.socketFactory
      val builder = OkHttpClient.Builder()
        .readTimeout(Duration.ofMinutes(1))
        .connectTimeout(Duration.ofMinutes(1))
      builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
      builder.hostnameVerifier { hostname: String?, session: SSLSession? -> true }
      builder.build()
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
}