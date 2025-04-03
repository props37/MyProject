package ru.livetyping.zarina.core.domain.model.geo

// Marked as stable on config/compose/stability_config.txt
public data class Building(
    override val id: KladrId,
    override val name: String,
) : AddressPart
