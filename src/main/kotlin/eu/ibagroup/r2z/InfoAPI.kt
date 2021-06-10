// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import retrofit2.Call
import retrofit2.http.GET

interface InfoAPI {

  @GET("zosmf/info")
  fun getSystemInfo() : Call<InfoResponse>

}