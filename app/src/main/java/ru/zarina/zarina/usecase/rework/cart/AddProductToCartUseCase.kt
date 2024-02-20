package ru.zarina.zarina.usecase.rework.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.cart.CartRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.common.Barcode
import ru.zarina.zarina.usecase.base.UseCase
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : UseCase<AddProductToCartUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(params: Params) {
        val barcode = params.barcode
        val count = params.count
        cartRepository.addProductToCart(barcode, count)
    }

    data class Params(val barcode: Barcode, val count: Int)
}
