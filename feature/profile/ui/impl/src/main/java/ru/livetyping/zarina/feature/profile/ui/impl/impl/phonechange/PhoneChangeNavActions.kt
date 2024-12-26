package ru.livetyping.zarina.feature.profile.ui.impl.impl.phonechange

import ru.livetyping.zarina.core.domain.model.common.PhoneNumber

internal class PhoneChangeNavActions(
    val onBackClicked: () -> Unit,
    val onPhoneChangeRequested: (PhoneNumber) -> Unit,
)
