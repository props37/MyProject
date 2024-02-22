package ru.zarina.zarina.usecase.rework.cart

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.cart.CartRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.usecase.base.FlowUseCase
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
