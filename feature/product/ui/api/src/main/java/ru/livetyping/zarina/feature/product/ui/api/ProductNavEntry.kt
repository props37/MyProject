package ru.livetyping.zarina.feature.product.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
public data class ProductNavEntry(val productId: String) : NavigationEntry
