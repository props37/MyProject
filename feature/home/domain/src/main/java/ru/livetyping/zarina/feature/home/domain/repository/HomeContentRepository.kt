package ru.livetyping.zarina.feature.home.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.feature.home.domain.model.HomeContent

public interface HomeContentRepository {
    public fun getOnboardingBannerUrl(): Url

    public fun getHomeContentFlow(): Flow<HomeContent>
}
