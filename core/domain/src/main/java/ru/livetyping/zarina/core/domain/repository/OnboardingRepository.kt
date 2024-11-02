package ru.livetyping.zarina.core.domain.repository

public interface OnboardingRepository {
    public suspend fun setIsOnboardingCompleted(isCompleted: Boolean)
}
