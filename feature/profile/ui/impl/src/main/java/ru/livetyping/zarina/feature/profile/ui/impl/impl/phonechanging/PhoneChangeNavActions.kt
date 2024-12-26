package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechanging

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal class PhoneChangeNavActions(
    val onBackClicked: () -> Unit,
    val onPhoneChangeRequested: (PhoneNumber) -> Unit,
)
