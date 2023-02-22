// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import eu.ibagroup.r2z.annotations.AvailableSince
import eu.ibagroup.r2z.annotations.ZVersion
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ConsoleAPI {

  @AvailableSince(ZVersion.ZOS_2_1)
  @PUT("/zosmf/restconsoles/consoles/{consolename}")
  fun issueCommand(
    @Header("Authorization") authorizationToken: String,
    @Header("Content-type") contentType: ContentType = ContentType.APP_JSON,
    @Path("consolename") consoleName: String,
    @Body body: IssueRequestBody
    ): Call<IssueResponse>

}