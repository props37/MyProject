package ru.livetyping.zarina.ui.screen.productcountselector

import ru.livetyping.zarina.usecase.cart.ChangeProductCountInCartUseCase
import javax.inject.Inject

class ProductCountSelectorInteractor @Inject constructor(
    val changeProductCountInCart: ChangeProductCountInCartUseCase,
)
