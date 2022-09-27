// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosuss.input

import eu.ibagroup.r2z.FilePath
import eu.ibagroup.r2z.SymlinkMode
import eu.ibagroup.r2z.XIBMAttr
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * This interface defines the options that can be sent into the list data set function
 */
class UssListParams(

    val limit: Int = 0,
    val lstat: Boolean = false,

    val group: String? = null,
    val mtime: String? = null,
    val name: String? = null,
    val size: String? = null,
    val perm: String? = null,
    val type: String? = null,
    val user: String? = null,

    val depth: Int = 1,
    val fileSystem: String? = null,
    val followSymlinks: SymlinkMode? = null

)
