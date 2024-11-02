package ru.livetyping.zarina.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url

public interface OnboardingRepository {
    public fun getOnboardingBannerUrlFlow(): Flow<Url>

    public suspend fun setIsOnboardingCompleted(isCompleted: Boolean)
}
