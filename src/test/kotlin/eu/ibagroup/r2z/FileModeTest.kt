/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright © 2021 IBA Group, a.s.
 */

package eu.ibagroup.r2z

import org.junit.jupiter.api.Test

internal class FileModeTest {

  @Test
  fun buildFromSequence() {
    val sequence = "drwxrwxrwx"
    val mode = FileMode(sequence)
    val modeFromDigits = FileMode(7,7,7)
    assert(mode.owner == 7 && mode.group == 7 && mode.all == 7)
    assert(modeFromDigits.owner == FileModeValue.READ_WRITE_EXECUTE.mode)
  }

}