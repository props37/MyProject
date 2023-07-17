package ru.zarina.zarina.data.content.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.content.remote.api.IZarinaContentApi

@Factory
class ZarinaContentRemoteSource(
    private val api: IZarinaContentApi,
) : IContentRemoteSource {
    override suspend fun getOnboardingSplash() = api.getOnboardingSplash().toDomain()
    override suspend fun getBanners() = api.getBanners().mapNotNull { it.toDomain() }
    override suspend fun getSelections() = api.getSelections().flatMap { it.toDomain() }
}
