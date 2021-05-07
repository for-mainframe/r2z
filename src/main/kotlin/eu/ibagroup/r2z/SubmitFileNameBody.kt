package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitFileNameBody (
    @SerializedName("file")
    @Expose
    var file: String
)