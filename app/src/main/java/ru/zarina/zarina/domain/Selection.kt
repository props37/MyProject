package ru.zarina.zarina.domain

import kotlinx.collections.immutable.ImmutableList

data class Selection(
    val title: String,
    val subtitle: String?,
    val banners: ImmutableList<Banner>,
    val products: ImmutableList<Product>,
)