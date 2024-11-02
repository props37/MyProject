package ru.livetyping.zarina.data.onboarding.impl

import ru.livetyping.zarina.core.domain.repository.OnboardingRepository
import ru.livetyping.zarina.data.onboarding.impl.local.OnboardingLocalDataSource
import javax.inject.Inject

internal class OnboardingRepositoryImpl @Inject constructor(
    private val localDataSource: OnboardingLocalDataSource,
) : OnboardingRepository {
    override suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        localDataSource.setIsOnboardingCompleted(isCompleted)
    }
}
