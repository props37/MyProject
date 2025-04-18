package ru.livetyping.zarina.core.domain.usecase.cart

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.product.Barcode
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface RemoveProductFromCartUseCase {
    public suspend operator fun invoke(params: Params): Result<Unit>

    public data class Params(
        val product: CartProduct,
        val barcode: Barcode,
    )

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            appMetrica: AppMetrica,
            logger: UseCaseLogger?,
        ): RemoveProductFromCartUseCase {
            return RemoveProductFromCartUseCaseImpl(
                cartRepository = cartRepository,
                appMetrica = appMetrica,
                logger = logger,
            )
        }
    }
}
