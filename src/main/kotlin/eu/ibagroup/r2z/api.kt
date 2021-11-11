// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val gson: Gson = GsonBuilder()
  .setLenient()
  .create()

inline fun <reified API> buildApi(baseUrl: String, httpClient: OkHttpClient): API {
  val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(httpClient)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
  return retrofit.create(API::class.java)
}

fun <API> buildApi(baseUrl: String, httpClient: OkHttpClient, apiClass: Class<out API>): API {
  val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(httpClient)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
  return retrofit.create(apiClass)
}

inline fun <reified Api> buildApiWithBytesConverter(baseUrl: String, httpClient: OkHttpClient): Api {
  val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(BytesConverterFactory.create())
    .client(httpClient)
    .build()
  return retrofit.create(Api::class.java)
}

fun <API> buildApiWithBytesConverter(baseUrl: String, httpClient: OkHttpClient, apiClass: Class<out API>): API {
  val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(BytesConverterFactory.create())
    .client(httpClient)
    .build()
  return retrofit.create(apiClass)
}
