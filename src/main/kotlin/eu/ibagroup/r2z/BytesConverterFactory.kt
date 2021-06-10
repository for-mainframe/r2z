// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class BytesConverterFactory : Converter.Factory() {
  override fun responseBodyConverter(
    type: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit
  ): Converter<ResponseBody, *>? {
    if (getRawType(type) !== ByteArray::class.java) return null

    return Converter<ResponseBody, ByteArray>{responseBody:ResponseBody->
      responseBody.byteStream().readAllBytes()
    }
  }
  companion object Factory {
    fun create(): BytesConverterFactory = BytesConverterFactory()
  }
}