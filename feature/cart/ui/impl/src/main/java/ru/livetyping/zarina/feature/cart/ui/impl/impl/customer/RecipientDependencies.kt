package ru.livetyping.zarina.feature.cart.ui.impl.impl.customer

import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import javax.inject.Inject

internal class RecipientDependencies @Inject constructor(
    val getUserFlow: GetUserFlowUseCase,
)
