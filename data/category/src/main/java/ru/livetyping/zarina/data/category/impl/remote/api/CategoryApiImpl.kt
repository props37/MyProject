package ru.livetyping.zarina.data.category.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.data.category.impl.remote.api.dto.CategoriesDto
import javax.inject.Inject

internal class CategoryApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : CategoryApi {
    override suspend fun getCategories(): CategoriesDto {
        return httpClient.get("/api/v1/categories").body()
    }
}
