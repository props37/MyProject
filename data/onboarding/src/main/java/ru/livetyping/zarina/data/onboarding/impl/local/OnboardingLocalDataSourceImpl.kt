package ru.livetyping.zarina.data.onboarding.impl.local

import javax.inject.Inject

internal class OnboardingLocalDataSourceImpl @Inject constructor(
    private val dataHolder: OnboardingDataHolder,
) : OnboardingLocalDataSource {
    override suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        dataHolder.setIsOnboardingCompleted(isCompleted)
    }
}
