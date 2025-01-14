package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.usecase.cart.RedeemBonusesUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class RedeemBonusesUseCaseImpl(
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, Unit>(logger), RedeemBonusesUseCase {

    override suspend fun execute(params: Params) {
        cartRepository.redeemBonuses(params.cartType, params.bonusCount)
    }

    override suspend fun invoke(params: Params): Result<Unit> {
        return call(params)
    }

    private companion object {
        private const val TAG = "RedeemBonusesUseCaseImpl"
    }
}
