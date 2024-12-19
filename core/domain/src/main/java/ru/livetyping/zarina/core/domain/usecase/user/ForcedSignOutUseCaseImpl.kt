package ru.livetyping.zarina.core.domain.usecase.user

import ru.livetyping.zarina.core.domain.impl.SignOutCleaner
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class ForcedSignOutUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository,
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository,
    logger: UseCaseLogger?,
) : UseCase<Unit, Unit>(logger), ForcedSignOutUseCase {

    override suspend fun execute(params: Unit) {
        val cleaner = SignOutCleaner(
            authRepository = authRepository,
            userRepository = userRepository,
            contentRepository = contentRepository,
            wishlistRepository = wishlistRepository,
            cartRepository = cartRepository,
        )
        cleaner.performSignOutCleanup()
    }

    override suspend fun invoke(): Result<Unit> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "ForcedSignOutUseCaseImpl"
    }
}
