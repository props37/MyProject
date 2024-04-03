package ru.livetyping.zarina.data.old.content

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.content.remote.IContentRemoteSource

@Factory
class ContentRepository(
    private val remoteSource: IContentRemoteSource,
) : IContentRepository {
    override suspend fun getOnboardingSplash() = remoteSource.getOnboardingSplash()
    override suspend fun getBanners() = remoteSource.getBanners()
    override fun getSelections() = remoteSource.getSelections()
}
