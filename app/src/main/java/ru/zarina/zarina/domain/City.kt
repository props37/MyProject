package ru.zarina.zarina.domain

data class City(
    val id: AddressId,
    val name: String,
    val region: String,
) {

    val priority: Int?
        get() = when (id) {
            AddressId.SAINT_PETERSBURG -> 0
            AddressId.MOSCOW -> 1
            else -> null
        }

    companion object {
        val DEFAULT
            get() = City(
                id = AddressId.SAINT_PETERSBURG,
                name = "Санкт-Петербург",
                region = "г. Санкт-Петербург",
            )
    }

}
