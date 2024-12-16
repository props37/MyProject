package ru.livetyping.zarina.feature.profile.ui.impl.impl.order

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
internal data class OrderNavEntry(val orderId: String) : NavigationEntry
