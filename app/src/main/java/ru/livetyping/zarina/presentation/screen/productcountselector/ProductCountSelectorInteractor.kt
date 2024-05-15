package ru.livetyping.zarina.presentation.screen.productcountselector

import ru.livetyping.zarina.usecase.cart.ChangeProductCountInCartUseCase
import javax.inject.Inject

class ProductCountSelectorInteractor @Inject constructor(
    val changeProductCountInCart: ChangeProductCountInCartUseCase,
)
