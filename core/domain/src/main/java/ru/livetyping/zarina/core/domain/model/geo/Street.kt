package ru.livetyping.zarina.core.domain.model.geo

// Marked as stable on config/compose/stability_config.txt
public data class Street(
    override val id: FiasId,
    override val name: String,
) : AddressPart
