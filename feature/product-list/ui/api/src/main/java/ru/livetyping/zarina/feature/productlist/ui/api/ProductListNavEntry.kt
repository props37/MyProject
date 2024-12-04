package ru.livetyping.zarina.feature.productlist.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
public data class ProductListNavEntry(
    val categoryId: String,
) : NavigationEntry
