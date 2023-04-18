package ru.zarina.zarina.data.content.remote

import ru.zarina.zarina.data.content.remote.api.IContentApi
import javax.inject.Inject

class ContentRemoteSource @Inject constructor(
    private val api: IContentApi,
) : IContentRemoteSource {
    override suspend fun getOnboardingSplash() = api.getOnboardingSplash().toDomain()
}
