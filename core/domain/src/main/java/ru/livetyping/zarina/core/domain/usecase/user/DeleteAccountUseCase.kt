package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface DeleteAccountUseCase {
    public suspend operator fun invoke(): Result<Unit>

    public companion object {
        public fun getInstance(
            userRepository: UserRepository,
            authRepository: AuthRepository,
            contentRepository: ContentRepository,
            wishlistRepository: WishlistRepository,
            logger: UseCaseLogger?,
        ): DeleteAccountUseCase {
            return DeleteAccountUseCaseImpl(
                userRepository = userRepository,
                authRepository = authRepository,
                contentRepository = contentRepository,
                wishlistRepository = wishlistRepository,
                logger = logger,
            )
        }
    }
}
