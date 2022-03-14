// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import retrofit2.Call
import retrofit2.http.*

interface TsoApi {

  @POST("/zosmf/tsoApp/tso")
  fun startTso(
    @Header("Authorization") authorizationToken: String,
    @Header("Content-type") contentType: ContentType = ContentType.APP_JSON,
    @Query("proc") proc: String,
    @Query("chset") chset: String,
    @Query("cpage") cpage: CodePage,
    @Query("rows") rows: Int,
    @Query("cols") cols: Int,
    @Query("acct") acct: String? = null,
    @Query("ugrp") ugrp: String? = null,
    @Query("rsize") rsize: Int? = null,
    @Query("appsessid") appsessid: String? = null
  ): Call<Void>

  @PUT("/zosmf/tsoApp/tso/{servletKey}")
  fun sendMessageToTso(
    @Header("Authorization") authorizationToken: String,
    @Header("Content-type") contentType: ContentType = ContentType.APP_JSON,
    @Body body: TsoData,
    @Path("servletKey") servletKey: String,
    @Query("readReply") readReply: Boolean? = null
  ): Call<Void>

  @GET("/zosmf/tsoApp/tso/{servletKey}")
  fun receiveMessagesFromTso(
    @Header("Authorization") authorizationToken: String,
    @Header("Content-type") contentType: ContentType = ContentType.APP_JSON,
    @Path("servletKey") servletKey: String,
  ): Call<TsoResponse>

  @DELETE("/zosmf/tsoApp/tso/{servletKey}")
  fun endTso(
    @Header("Authorization") authorizationToken: String,
    @Header("Content-type") contentType: ContentType = ContentType.APP_JSON,
    @Path("servletKey") servletKey: String,
    @Query("tsoforcecancel") tsoForceCancel: Boolean? = null
  ): Call<TsoResponse>

}