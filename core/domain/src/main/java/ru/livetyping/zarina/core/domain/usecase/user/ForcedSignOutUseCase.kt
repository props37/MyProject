package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.repository.SearchRepository
import ru.livetyping.zarina.core.domain.repository.StoreRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface ForcedSignOutUseCase {
    public suspend operator fun invoke(): Result<Unit>

    public companion object {
        public fun getInstance(
            authRepository: AuthRepository,
            userRepository: UserRepository,
            contentRepository: ContentRepository,
            wishlistRepository: WishlistRepository,
            cartRepository: CartRepository,
            storeRepository: StoreRepository,
            searchRepository: SearchRepository,
            logger: UseCaseLogger?,
        ): ForcedSignOutUseCase {
            return ForcedSignOutUseCaseImpl(
                authRepository = authRepository,
                userRepository = userRepository,
                contentRepository = contentRepository,
                wishlistRepository = wishlistRepository,
                cartRepository = cartRepository,
                storeRepository = storeRepository,
                searchRepository = searchRepository,
                logger = logger,
            )
        }
    }
}
