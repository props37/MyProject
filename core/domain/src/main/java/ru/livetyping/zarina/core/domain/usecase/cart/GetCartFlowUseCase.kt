package ru.livetyping.zarina.core.domain.usecase.cart

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCartFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<Cart>>

    public data class Params(val cartType: CartType)

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            userRepository: UserRepository,
            wishlistRepository: WishlistRepository,
            logger: UseCaseLogger?,
        ): GetCartFlowUseCase {
            return GetCartFlowUseCaseImpl(
                cartRepository = cartRepository,
                userRepository = userRepository,
                wishlistRepository = wishlistRepository,
                logger = logger,
            )
        }
    }
}
