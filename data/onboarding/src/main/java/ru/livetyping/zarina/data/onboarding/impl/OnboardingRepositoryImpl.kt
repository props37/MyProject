package ru.livetyping.zarina.data.onboarding.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.repository.OnboardingRepository
import ru.livetyping.zarina.data.onboarding.impl.local.OnboardingLocalDataSource
import ru.livetyping.zarina.data.onboarding.impl.remote.OnboardingRemoteDataSource
import javax.inject.Inject

internal class OnboardingRepositoryImpl @Inject constructor(
    private val localDataSource: OnboardingLocalDataSource,
    private val remoteDataSource: OnboardingRemoteDataSource,
) : OnboardingRepository {
    override fun getOnboardingBannerUrlFlow(): Flow<Url> {
        return remoteDataSource.getOnboardingBannerUrlFlow()
    }

    override suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        localDataSource.setIsOnboardingCompleted(isCompleted)
    }
}
