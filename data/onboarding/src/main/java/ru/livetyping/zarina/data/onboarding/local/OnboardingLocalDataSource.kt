package ru.livetyping.zarina.data.onboarding.local

import kotlinx.coroutines.flow.Flow

internal interface OnboardingLocalDataSource {
    fun getIsOnboardingCompleted(): Flow<Boolean>

    suspend fun setIsOnboardingCompleted(isCompleted: Boolean)
}
