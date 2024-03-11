package ru.zarina.zarina.usecase.cart

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.data.cart.CartRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.rework.product.Product
import javax.inject.Inject

class GetCartProductIdsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : FlowUseCase<Unit, Set<Product.Id>>(dispatcher) {

    override fun execute(params: Unit): Flow<Set<Product.Id>> {
        return cartRepository.cartProductIds
    }
}
