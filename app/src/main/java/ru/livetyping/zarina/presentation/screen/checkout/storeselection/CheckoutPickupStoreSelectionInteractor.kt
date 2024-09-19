package ru.livetyping.zarina.presentation.screen.checkout.storeselection

import ru.livetyping.zarina.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.usecase.checkout.GetPickupStoresFlowUseCase
import ru.livetyping.zarina.usecase.user.GetUserCityFlowUseCase
import javax.inject.Inject

class CheckoutPickupStoreSelectionInteractor @Inject constructor(
    val getCartFlow: GetCartFlowUseCase,
    val getPickupStoresFlow: GetPickupStoresFlowUseCase,
    val getUserCityFlow: GetUserCityFlowUseCase,
)
