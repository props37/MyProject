package ru.zarina.zarina.domain.rework.geography

data class City(
    val addressId: AddressId,
    val name: String,
) {
    companion object {
        val SAINT_PETERSBURG: City
            get() = City(
                addressId = AddressId.SAINT_PETERSBURG,
                name = "Санкт-Петербург",
            )
    }
}
