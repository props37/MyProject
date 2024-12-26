package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechange.model

internal sealed interface PhoneChangeEvent {
    data object BackClicked : PhoneChangeEvent

    data object RequestPhoneChangeClicked : PhoneChangeEvent
}
