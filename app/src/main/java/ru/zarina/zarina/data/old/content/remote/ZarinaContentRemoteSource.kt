package ru.zarina.zarina.data.old.content.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.content.remote.api.IZarinaContentApi
import ru.zarina.zarina.domain.old.Selection

@Factory
class ZarinaContentRemoteSource(
    private val api: IZarinaContentApi,
) : IContentRemoteSource {
    override suspend fun getOnboardingSplash() = api.getOnboardingSplash().toDomain()
    override suspend fun getBanners() = api.getBanners().mapNotNull { it.toDomain() }
    override fun getSelections(): Flow<List<Selection>> =
        flow { emit(api.getSelections().flatMap { it.toDomain() }) }
}
