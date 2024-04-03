package ru.livetyping.zarina.domain.old

import kotlinx.collections.immutable.ImmutableList

sealed interface Selection {
    data class Banners(
        val banners: ImmutableList<Banner>
    ) : Selection

    data class Products(
        val title: String?,
        val subtitle: String?,
        val products: ImmutableList<Product>
    ) : Selection
}
