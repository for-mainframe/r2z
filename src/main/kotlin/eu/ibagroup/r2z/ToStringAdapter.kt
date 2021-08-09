// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * For this project only
 */
class ToStringAdapter: TypeAdapter<Any>() {

  override fun write(out: JsonWriter, value: Any) {
    out.value(value.toString())
  }

  override fun read(`in`: JsonReader): Any {
    TODO("Not yet implemented")
  }


}