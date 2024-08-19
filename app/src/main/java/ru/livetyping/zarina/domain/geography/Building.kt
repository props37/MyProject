package ru.livetyping.zarina.domain.geography

data class Building(
    override val id: KladrId,
    override val name: String,
) : AddressPart
