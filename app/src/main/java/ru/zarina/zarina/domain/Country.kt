package ru.zarina.zarina.domain

data class Country(
    val id: String,
    val name: String,
    val cities: List<City>,
)
