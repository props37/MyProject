package ru.livetyping.zarina.core.domain.model.geo

// Marked as stable on config/compose/stability_config.txt
public data class City(
    override val id: KladrId,
    override val name: String,
    val fullName: String?,
    val region: String?,
) : AddressPart {
    public companion object {
        public val SAINT_PETERSBURG: City
            get() = City(
                name = "Санкт-Петербург",
                id = KladrId.SAINT_PETERSBURG,
                fullName = "г Санкт-Петербург",
                region = "г Санкт-Петербург",
            )

        public fun getDefault(): City = SAINT_PETERSBURG
    }
}
