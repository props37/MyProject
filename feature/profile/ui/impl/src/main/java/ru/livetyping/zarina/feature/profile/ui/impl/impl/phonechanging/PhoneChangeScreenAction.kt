package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal sealed interface PhoneChangeScreenAction {
    data object BackClicked : PhoneChangeScreenAction

    data class PhoneChangeRequested(val phone: PhoneNumber) : PhoneChangeScreenAction
}
