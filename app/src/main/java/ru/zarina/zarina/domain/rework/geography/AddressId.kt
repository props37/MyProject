package ru.zarina.zarina.domain.rework.geography

/**
 * Address ID according to "КЛАДР РФ"
 */
@JvmInline
value class AddressId(val value: String) {
    companion object {
        val MOSCOW = AddressId("7700000000000")
        val SAINT_PETERSBURG = AddressId("7800000000000")
    }
}
