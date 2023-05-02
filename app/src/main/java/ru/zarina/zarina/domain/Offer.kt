package ru.zarina.zarina.domain

data class Offer(
    val id: String,
    val isAvailable: Boolean,
    val barcode: String,
    val size: Size,
)
