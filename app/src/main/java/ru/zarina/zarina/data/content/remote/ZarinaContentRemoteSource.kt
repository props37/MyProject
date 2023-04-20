package ru.zarina.zarina.data.content.remote

import ru.zarina.zarina.data.content.remote.api.IZarinaContentApi
import javax.inject.Inject

class ZarinaContentRemoteSource @Inject constructor(
    private val api: IZarinaContentApi,
) : IContentRemoteSource {
    override suspend fun getOnboardingSplash() = api.getOnboardingSplash().toDomain()
}
