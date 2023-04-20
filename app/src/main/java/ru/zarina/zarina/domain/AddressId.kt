package ru.zarina.zarina.domain

/**
 * Address id according to "КЛАДР РФ"
 */
@JvmInline
value class AddressId(val id: String) {

    companion object {
        val SAINT_PETERSBURG = AddressId("7800000000000")
        val MOSCOW = AddressId("7700000000000")
    }

}
