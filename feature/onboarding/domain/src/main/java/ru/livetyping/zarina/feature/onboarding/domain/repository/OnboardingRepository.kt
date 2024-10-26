package ru.livetyping.zarina.feature.onboarding.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url

public interface OnboardingRepository {
    public fun getOnboardingBannerUrlFlow(): Flow<Url>
}
