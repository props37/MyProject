package ru.livetyping.zarina.core.domain.model.geo

public data class City(
    override val id: KladrId,
    override val name: String,
    val fullName: String?,
    val region: String?,
) : AddressPart {
    public companion object {
        public val DEFAULT: City
            get() = SAINT_PETERSBURG

        public val SAINT_PETERSBURG: City
            get() = City(
                name = "Санкт-Петербург",
                id = KladrId.SAINT_PETERSBURG,
                fullName = "г Санкт-Петербург",
                region = "г Санкт-Петербург",
            )
    }
}
