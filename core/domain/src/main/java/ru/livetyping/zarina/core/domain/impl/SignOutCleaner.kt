package ru.livetyping.zarina.core.domain.impl

import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.CartRepository
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.repository.WishlistRepository

internal class SignOutCleaner(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository,
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository,
) {
    suspend fun performSignOutCleanup() {
        authRepository.clear()
        userRepository.clear()
        contentRepository.clear()
        wishlistRepository.clear()
        cartRepository.clear()
        // TODO: [Top] Clear ProductSearchRepository
        // TODO: [Top] Clear StoreRepository
        // TODO: [Top] Clear CheckoutRepository
    }
}
