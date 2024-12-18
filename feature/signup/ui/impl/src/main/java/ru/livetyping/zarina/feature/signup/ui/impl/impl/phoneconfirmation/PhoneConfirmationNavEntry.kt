package ru.livetyping.zarina.feature.signup.ui.impl.impl.phoneconfirmation

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.navigation.NavigationEntry

@Serializable
internal data class PhoneConfirmationNavEntry(val phone: String) : NavigationEntry
