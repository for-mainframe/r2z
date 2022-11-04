// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TsoResponse(
  @SerializedName("servletKey")
  @Expose
  var servletKey: String? = null,

  @SerializedName("ver")
  @Expose
  var ver: Int? = null,

  @SerializedName("queueID")
  @Expose
  var queueId: String? = null,

  @SerializedName("remoteSys")
  @Expose
  var remoteSys: String? = null,

  @SerializedName("tsoData")
  @Expose
  var tsoData: List<TsoData> = emptyList(),

  @SerializedName("appData")
  @Expose
  var appData: String? = null,

  @SerializedName("timeout")
  @Expose
  var timeout: Boolean? = null,

  @SerializedName("reused")
  @Expose
  var reused: Boolean? = null,

  @SerializedName("msgData")
  @Expose
  var msgData: List<MessageData> = emptyList(),

  @SerializedName("messages")
  @Expose
  var messages: String? = null,
)

data class TsoData(
  @SerializedName(value = "TSO MESSAGE")
  @Expose
  var tsoMessage: MessageType? = null,

  @SerializedName(value = "TSO PROMPT")
  @Expose
  var tsoPrompt: MessageType? = null,

  @SerializedName(value = "TSO RESPONSE")
  @Expose
  var tsoResponse: MessageType? = null,
)

data class MessageType(
  @SerializedName("VERSION")
  @Expose
  var version: String,

  @SerializedName(value = "DATA")
  @Expose
  var data: String? = null,

  @SerializedName(value = "HIDDEN")
  @Expose
  var hidden: String? = null,

  @SerializedName(value = "ACTION")
  @Expose
  var action: String? = null,
)

data class MessageData(
  @SerializedName("messageText")
  @Expose
  var messageText: String? = null,

  @SerializedName("messageId")
  @Expose
  var messageId: String? = null,
)

enum class TsoCodePage(val codePage: String) {
 IBM_1025("1025"),
 IBM_1047("1047");

 override fun toString(): String {
   return codePage
 }
}
