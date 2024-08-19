package ru.livetyping.zarina.domain.geography

data class City(
    override val name: String,
    override val id: KladrId,
    val fullName: String,
    val region: String,
) : AddressPart {
    companion object {
        val DEFAULT: City
            get() = SAINT_PETERSBURG

        private val SAINT_PETERSBURG: City
            get() = City(
                name = "Санкт-Петербург",
                id = KladrId.SAINT_PETERSBURG,
                fullName = "г Санкт-Петербург",
                region = "г Санкт-Петербург",
            )
    }
}
