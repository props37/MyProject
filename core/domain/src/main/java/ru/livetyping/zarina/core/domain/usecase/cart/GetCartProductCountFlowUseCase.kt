package ru.livetyping.zarina.core.domain.usecase.cart

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCartProductCountFlowUseCase {
    public operator fun invoke(): Flow<Result<Int>>

    public companion object {
        public fun getInstance(
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): GetCartProductCountFlowUseCase {
            return GetCartProductCountFlowUseCaseImpl(
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}
