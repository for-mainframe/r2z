// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

data class HoldJobRequestBody (
    @SerializedName("request")
    @Expose
    var requestType: RequestTypes = RequestTypes.HOLD,

    @SerializedName("version")
    @Expose
    @JsonAdapter(ToStringAdapter::class)
    var requestVersion: RequestVersion = RequestVersion.SYNCHRONOUS
)

data class ReleaseJobRequestBody (
    @SerializedName("request")
    @Expose
    var requestType: RequestTypes = RequestTypes.RELEASE,

    @SerializedName("version")
    @Expose
    @JsonAdapter(ToStringAdapter::class)
    var requestVersion: RequestVersion = RequestVersion.SYNCHRONOUS
)

data class CancelJobRequestBody (
    @SerializedName("request")
    @Expose
    var requestType: RequestTypes = RequestTypes.CANCEL,

    @SerializedName("version")
    @Expose
    @JsonAdapter(ToStringAdapter::class)
    var requestVersion: RequestVersion = RequestVersion.SYNCHRONOUS
)

enum class RequestTypes(val value: String) {
    HOLD("hold"),
    RELEASE("release"),
    CANCEL("cancel");


    override fun toString(): String {
        return value
    }
}

enum class RequestVersion(val value: String) {
    ASYNCHRONOUS("1.0"),
    SYNCHRONOUS("2.0");


    override fun toString(): String {
        return value
    }
}
