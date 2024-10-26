package ru.livetyping.zarina.data.content.impl

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.data.content.impl.local.ContentLocalDataSource
import ru.livetyping.zarina.data.content.impl.remote.ContentRemoteDataSource
import ru.livetyping.zarina.feature.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

internal class ContentRepositoryImpl @Inject constructor(
    private val localDataSource: ContentLocalDataSource,
    private val remoteDataSource: ContentRemoteDataSource,
) : ContentRepository, OnboardingRepository {
    override fun getLastContentGenderFlow(): Flow<Gender?> {
        return localDataSource.getLastContentGenderFlow()
    }

    override suspend fun setLastContentGender(gender: Gender) {
        localDataSource.setLastContentGender(gender)
    }

    override fun getOnboardingBannerUrlFlow(): Flow<Url> {
        return remoteDataSource.getOnboardingBannerUrlFlow()
    }
}
