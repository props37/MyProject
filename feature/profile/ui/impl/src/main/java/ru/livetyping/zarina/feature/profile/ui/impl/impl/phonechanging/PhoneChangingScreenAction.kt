package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal sealed interface PhoneChangingScreenAction {
    data object BackClicked : PhoneChangingScreenAction

    data class PhoneChangeRequested(val phone: PhoneNumber) : PhoneChangingScreenAction
}
