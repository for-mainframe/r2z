// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import eu.ibagroup.r2z.annotations.ZVersion

/**
 * A data class which represents a systems response from z/OSMF instance
 */

data class SystemsResponse(

    /**
     * @param items - a serialized list of SystemZOSInfo instances defined to z/OSMF
     */
    @SerializedName("items")
    @Expose
    val items: List<SystemZOSInfo> = emptyList(),

    /**
     * @param numRows - a serialized number of returned objects
     */
    @SerializedName("numRows")
    @Expose
    val numRows: Int
) {

    fun getZOSVersion() : ZVersion {
        var system = items.last()
        var version = system.zosVR.substring(5)
        return when (version) {
            "V2R1" -> ZVersion.ZOS_2_1
            "V2R2" -> ZVersion.ZOS_2_2
            "V2R3" -> ZVersion.ZOS_2_3
            "V2R4" -> ZVersion.ZOS_2_4
            else -> ZVersion.ZOS_2_1
        }
    }
}

/**
 * A data class which represents a single SystemZOSInfo instance defined to z/OSMF
 */

data class SystemZOSInfo(

    /**
     * @param systemNickName - a serialized unique name assigned to the system definition
     */
    @SerializedName("systemNickName")
    @Expose
    val systemNickName: String = "null",

    /**
     * @param systemName - a serialized name specified for the system on the SYSNAME parameter in the IEASYSxx parmlib member
     */
    @SerializedName("systemName")
    @Expose
    val systemName: String = "null",

    /**
     * @param sysplexName - a serialized name of the sysplex where the z/OS® system is a member
     */
    @SerializedName("sysplexName")
    @Expose
    val sysplexName: String = "null",

    /**
     * @param groupNames - a serialized comma-separated list of the groups to which the system is assigned
     */
    @SerializedName("groupNames")
    @Expose
    val groupNames: String = "null",

    /**
     * @param url - a serialized URL used to access the z/OSMF instance that resides in the same sysplex as the system identified by the systemName attribute
     */
    @SerializedName("url")
    @Expose
    val url: String = "null",

    /**
     * @param zosVR - a serialized version and release of the z/OS image installed on the system
     */
    @SerializedName("zosVR")
    @Expose
    val zosVR: String = "null",

    /**
     * @param jesMemberName - a serialized JES2 multi-access spool (MAS) member name or JES3 complex member name
     * that is assigned to the primary job entry subsystem (JES) that is running on the system
     */
    @SerializedName("jesMemberName")
    @Expose
    val jesMemberName: String = "null",

    /**
     * @param jesType - a serialized type for the primary job entry subsystem running on the system. The type is either JES2 or JES3
     */
    @SerializedName("jesType")
    @Expose
    val jesType: String = "null",

    /**
     * @param cpcName - a serialized name specified for the central processor complex (CPC) at the support element (SE) of that processor complex
     */
    @SerializedName("cpcName")
    @Expose
    val cpcName: String = "null",

    /**
     * @param cpcSerial - a serialized serial number of the CPC
     */
    @SerializedName("spcSerial")
    @Expose
    val cpcSerial: String = "null",

    /**
     * @param httpProxyName - a serialized name of the HTTP proxy definition that specifies the settings required to access the system
     * through an HTTP or SOCKS proxy server
     */
    @SerializedName("httpProxyName")
    @Expose
    val httpProxyName: String = "null",

    /**
     * @param ftpDestinationName - a serialized name of the server definition that specifies the settings required to access the FTP or SFTP server
     * that is running on the system
     */
    @SerializedName("ftpDestinationName")
    @Expose
    val ftpDestinationName: String = "null"
)
