// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.zosfiles.input

/**
 * This interface defines the options that can be sent into the download data set function
 */
class DownloadParams(

  /**
   * The local file to download the data set to, e.g. "./path/to/file.txt"
   */
  val file: String ?= null,

  /**
   * The indicator to force return of ETag.
   * If set to 'true' it forces the response to include an "ETag" header, regardless of the size of the response data.
   * If it is not present, the default is to only send an Etag for data sets smaller than a system determined length,
   * which is at least 8 MB.
   */
  val returnEtag: Boolean ?= null,

  /**
   * The volume on which the data set is stored
   */
  val volume: String ?= null,

)