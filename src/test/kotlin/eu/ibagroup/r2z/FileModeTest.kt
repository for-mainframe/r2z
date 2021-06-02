// Copyright © 2020 IBA Group, a.s. All rights reserved. Use of this source code is governed by Eclipse Public License – v 2.0 that can be found at: https://www.eclipse.org/legal/epl-2.0/

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