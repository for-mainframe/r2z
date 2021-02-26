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