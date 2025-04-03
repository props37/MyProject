package ru.livetyping.zarina.core.domain.model.geo

public data class Address(
    val city: City,
    val street: Street,
    val building: Building,
    val apartment: String?,
)
