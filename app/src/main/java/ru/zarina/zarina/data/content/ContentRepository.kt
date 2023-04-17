package ru.zarina.zarina.data.content

import ru.zarina.zarina.data.content.remote.IContentRemoteSource
import javax.inject.Inject

class ContentRepository @Inject constructor(
    private val remoteSource: IContentRemoteSource,
) : IContentRepository {
    override suspend fun getOnboardingSplash() = remoteSource.getOnboardingSplash()
}

