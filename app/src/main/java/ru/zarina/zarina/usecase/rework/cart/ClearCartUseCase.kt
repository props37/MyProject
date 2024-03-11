package ru.zarina.zarina.usecase.rework.cart

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.cart.CartRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.base.usecase.UseCase
import timber.log.Timber
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val cartRepository: CartRepository,
) : UseCase<Unit, Unit>(dispatcher) {

    override suspend fun execute(params: Unit) {
        Timber.v("Clear cart")
        cartRepository.clearCart()
    }
}
