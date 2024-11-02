package ru.livetyping.zarina.data.onboarding.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.data.onboarding.impl.remote.api.OnboardingApi
import javax.inject.Inject

internal class OnboardingRemoteDataSourceImpl @Inject constructor(
    private val api: OnboardingApi,
) : OnboardingRemoteDataSource {
    override fun getOnboardingBannerUrlFlow(): Flow<Url> = flow {
        val url = api.getOnboardingBannerUrl()
        emit(url)
    }
}
