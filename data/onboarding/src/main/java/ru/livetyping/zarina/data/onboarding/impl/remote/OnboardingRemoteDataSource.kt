package ru.livetyping.zarina.data.onboarding.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url

internal interface OnboardingRemoteDataSource {
    fun getOnboardingBannerUrlFlow(): Flow<Url>
}
