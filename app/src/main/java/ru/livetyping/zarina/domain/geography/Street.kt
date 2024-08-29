package ru.livetyping.zarina.domain.geography

data class Street(
    override val id: KladrId,
    override val name: String,
) : AddressPart
