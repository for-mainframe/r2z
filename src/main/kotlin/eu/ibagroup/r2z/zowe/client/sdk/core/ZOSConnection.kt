// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

package eu.ibagroup.r2z.zowe.client.sdk.core

/**
 * z/OS Connection information placeholder
 *
 * @author Frank Giordano
 * @version 1.0
 */
class ZOSConnection(
  /**
   * machine host pointing to backend z/OS instance
   */
  val host: String,
  /**
   * machine host z/OSMF port number pointing to backend z/OS instance
   */
  val zosmfPort: String,
  /**
   * machine host username with access to backend z/OS instance
   */
  val user: String,
  /**
   * machine host username's password with access to backend z/OS instance
   */
  val password: String,
  /**
   * machine host z/OSMF protocol to connect to z/OS instance
   */
  val protocol: String = "https"
) {
  // TODO: doc
  fun checkConnection() {
    if (host.isEmpty() || password.isEmpty() || user.isEmpty()) {
      throw IllegalStateException("Connection data not setup properly")
    }
  }
  override fun toString()= "ZOSConnection{host='$host', zosmfPort='$zosmfPort', user='$user', password='$password'}"
}
