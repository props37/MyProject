package ru.livetyping.zarina.data.content.impl.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.data.content.impl.remote.api.ContentApi
import javax.inject.Inject

internal class ContentRemoteDataSourceImpl @Inject constructor(
    private val api: ContentApi,
) : ContentRemoteDataSource {
    override fun getOnboardingBannerUrlFlow(): Flow<Url> = flow {
        val url = api.getOnboardingBannerUrl()
        emit(url)
    }
}
