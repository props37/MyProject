package ru.livetyping.zarina.data.onboarding.impl.local

internal interface OnboardingDataHolder {
    suspend fun setIsOnboardingCompleted(isCompleted: Boolean)
}
