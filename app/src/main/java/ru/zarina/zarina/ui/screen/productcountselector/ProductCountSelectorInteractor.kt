package ru.zarina.zarina.ui.screen.productcountselector

import ru.zarina.zarina.usecase.cart.ChangeProductCountInCartUseCase
import javax.inject.Inject

class ProductCountSelectorInteractor @Inject constructor(
    val changeProductCountInCart: ChangeProductCountInCartUseCase,
)
