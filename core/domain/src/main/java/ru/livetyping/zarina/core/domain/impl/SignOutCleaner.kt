package ru.livetyping.zarina.core.domain.impl

import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository

internal class SignOutCleaner(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository,
    private val wishlistRepository: WishlistRepository,
) {
    suspend fun performSignOutCleanup() {
        authRepository.clear()
        userRepository.clear()
        contentRepository.clear()
        // TODO: [Top] Clear CartRepository
        wishlistRepository.clear()
        // TODO: [Top] Clear ProductSearchRepository
        // TODO: [Top] Clear StoreRepository
        // TODO: [Top] Clear CheckoutRepository
    }
}
