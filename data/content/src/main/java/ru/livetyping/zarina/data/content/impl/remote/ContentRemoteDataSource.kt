package ru.livetyping.zarina.data.content.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Url

internal interface ContentRemoteDataSource {
    fun getOnboardingBannerUrlFlow(): Flow<Url>
}
