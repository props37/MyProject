package ru.livetyping.zarina.data.onboarding.impl.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OnboardingLocalDataSourceImpl @Inject constructor(
    private val dataHolder: OnboardingDataHolder,
) : OnboardingLocalDataSource {
    override fun getIsOnboardingCompleted(): Flow<Boolean> {
        return dataHolder.getIsOnboardingCompleted()
    }

    override suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        dataHolder.setIsOnboardingCompleted(isCompleted)
    }
}
