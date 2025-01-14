package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.usecase.cart.ApplyMyCardUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ApplyMyCardUseCaseImpl(
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), ApplyMyCardUseCase {

    override suspend fun execute(params: Params) {
        cartRepository.applyMyCard(params.cartType, params.productsFirstPriceSum)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "ApplyMyCardUseCaseImpl"
    }
}
