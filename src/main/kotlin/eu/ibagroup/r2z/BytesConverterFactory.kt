// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import okhttp3.MediaType
import okhttp3.RequestBody
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

    return Converter { it.byteStream().readBytes() }
  }

  override fun requestBodyConverter(
    type: Type,
    parameterAnnotations: Array<out Annotation>,
    methodAnnotations: Array<out Annotation>,
    retrofit: Retrofit
  ): Converter<*, RequestBody>? {
    if (getRawType(type) !== ByteArray::class.java) return null

    return Converter<ByteArray, RequestBody> { RequestBody.create(MediaType.get("application/octet-stream"), it) }
  }
  companion object Factory {
    fun create(): BytesConverterFactory = BytesConverterFactory()
  }
}
