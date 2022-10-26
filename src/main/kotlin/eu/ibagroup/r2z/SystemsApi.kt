// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import eu.ibagroup.r2z.annotations.AvailableSince
import eu.ibagroup.r2z.annotations.ZVersion
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SystemsApi {

    /**
     * An API function to get all available systems defined to z/OSMF
     * @param authToken - is a base 64 encoding representation of <userid>:<password>
     * @return a wrapped instance of SystemsResponse
     */
    @AvailableSince(ZVersion.ZOS_2_1)
    @GET("zosmf/resttopology/systems")
    fun getSystems(
        @Header("Authorization") authToken: String,
    ): Call<SystemsResponse>

}

