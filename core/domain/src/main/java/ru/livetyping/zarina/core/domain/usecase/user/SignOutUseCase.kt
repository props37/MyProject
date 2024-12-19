package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface SignOutUseCase {
    public suspend operator fun invoke(): Result<Unit>

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            authRepository: AuthRepository,
            contentRepository: ContentRepository,
            wishlistRepository: WishlistRepository,
            cartRepository: CartRepository,
            logger: UseCaseLogger?,
        ): SignOutUseCase {
            return SignOutUseCaseImpl(
                userRepository = userRepository,
                authRepository = authRepository,
                contentRepository = contentRepository,
                wishlistRepository = wishlistRepository,
                cartRepository = cartRepository,
                logger = logger,
            )
        }
    }
}
