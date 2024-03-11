package ru.zarina.zarina.data.category.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.category.remote.api.dto.CategoriesDto
import ru.zarina.zarina.di.Qualifiers
import javax.inject.Inject

class CategoryApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getCategories(): CategoriesDto {
        return httpClient.get("/api/v1/categories").body()
    }
}
