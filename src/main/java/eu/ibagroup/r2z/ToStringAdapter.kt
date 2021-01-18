package eu.ibagroup.r2z

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * For shitty purposes only
 */
class ToStringAdapter: TypeAdapter<Any>() {

  override fun write(out: JsonWriter, value: Any) {
    out.value(value.toString())
  }

  override fun read(`in`: JsonReader): Any {
    TODO("Not yet implemented")
  }


}