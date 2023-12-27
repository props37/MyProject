package ru.zarina.zarina.domain.rework.geography

/**
 * According to "КЛАДР РФ"
 */
@JvmInline
value class KladrId(val value: String) {
    companion object {
        val MOSCOW: KladrId
            get() = KladrId("7700000000000")

        val SAINT_PETERSBURG: KladrId
            get() = KladrId("7800000000000")
    }
}
