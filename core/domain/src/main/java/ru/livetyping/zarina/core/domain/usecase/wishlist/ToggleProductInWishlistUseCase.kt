package ru.livetyping.zarina.core.domain.usecase.wishlist

import ru.livetyping.zarina.core.analytics.AppMetrica
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import ru.livetyping.zarina.core.domain.model.cart.CartProduct as DomainCartProduct
import ru.livetyping.zarina.core.domain.model.product.Product as DomainProduct

public interface ToggleProductInWishlistUseCase {
    public suspend operator fun invoke(params: Params): Result<Boolean>

    public sealed class Params {
        public abstract val productId: DomainProduct.Id

        public data class Product(val product: DomainProduct) : Params() {
            override val productId: DomainProduct.Id
                get() = product.id
        }

        public data class CartProduct(val product: DomainCartProduct) : Params() {
            override val productId: DomainProduct.Id
                get() = product.productId
        }
    }

    public companion object {
        public fun getInstance(
            wishlistRepository: WishlistRepository,
            appMetrica: AppMetrica,
            logger: UseCaseLogger?,
        ): ToggleProductInWishlistUseCase {
            return ToggleProductInWishlistUseCaseImpl(
                wishlistRepository = wishlistRepository,
                appMetrica = appMetrica,
                logger = logger,
            )
        }
    }
}
