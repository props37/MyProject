package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface RedeemBonusesUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val cartType: CartType,
        val bonusCount: Int,
    )

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            appMetrica: AppMetrica,
            logger: UseCaseLogger?,
        ): RedeemBonusesUseCase {
            return RedeemBonusesUseCaseImpl(
                cartRepository = cartRepository,
                appMetrica = appMetrica,
                logger = logger,
            )
        }
    }
}
