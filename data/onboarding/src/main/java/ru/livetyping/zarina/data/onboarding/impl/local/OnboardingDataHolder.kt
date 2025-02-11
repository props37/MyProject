package ru.livetyping.zarina.data.onboarding.impl.local

import kotlinx.coroutines.flow.Flow

internal interface OnboardingDataHolder {
    fun getIsOnboardingCompleted(): Flow<Boolean>

    suspend fun setIsOnboardingCompleted(isCompleted: Boolean)
}
