package ru.livetyping.zarina.core.domain.impl

import ru.livetyping.zarina.core.domain.model.auth.BearerTokens
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.repository.AuthRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository

internal class UserManager(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend fun setUserWithTokens(user: User, tokens: BearerTokens) {
        authRepository.setBearerTokens(tokens)
        try {
            userRepository.setUser(user)
        } catch (e: Exception) {
            authRepository.setBearerTokens(null)
            throw e
        }
    }
}
