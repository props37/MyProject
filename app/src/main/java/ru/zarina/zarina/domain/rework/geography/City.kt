package ru.zarina.zarina.domain.rework.geography

data class City(
    val name: String,
    val fullName: String,
    val region: String,
    val kladrId: KladrId,
) {
    companion object {
        val DEFAULT: City
            get() = SAINT_PETERSBURG

        private val SAINT_PETERSBURG: City
            get() = City(
                name = "Санкт-Петербург",
                fullName = "г Санкт-Петербург",
                region = "г Санкт-Петербург",
                kladrId = KladrId.SAINT_PETERSBURG,
            )
    }
}
