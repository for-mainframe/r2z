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