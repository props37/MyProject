package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ClearCartUseCaseImpl(
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : UseCase<Unit, Unit>(logger), ClearCartUseCase {

    override suspend fun execute(params: Unit) {
        cartRepository.clearCart()
    }

    override suspend fun invoke(): Result<Unit> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "ClearCartUseCaseImpl"
    }
}
