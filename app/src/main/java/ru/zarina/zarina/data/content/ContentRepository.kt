package ru.zarina.zarina.data.content

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.content.remote.IContentRemoteSource

@Factory
class ContentRepository(
    private val remoteSource: IContentRemoteSource,
) : IContentRepository {
    override suspend fun getOnboardingSplash() = remoteSource.getOnboardingSplash()
    override suspend fun getBanners() = remoteSource.getBanners()
    override suspend fun getSelections() = remoteSource.getSelections()
}
