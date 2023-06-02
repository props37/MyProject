package ru.zarina.zarina.utils.kotlin

import org.junit.Assert.assertEquals
import org.junit.Test

class IntKtTest {

    @Test
    fun roundToMultipleOf() {
        checkRoundToMultipleOf(0, 0, 0)
        checkRoundToMultipleOf(5, 2, 4)
        checkRoundToMultipleOf(-12, 5, -10)
        checkRoundToMultipleOf(100, 10, 100)
    }

    private fun checkRoundToMultipleOf(value: Int, multipleOf: Int, expected: Int) {
        assertEquals(expected, value.roundToMultipleOf(multipleOf))
    }

}
