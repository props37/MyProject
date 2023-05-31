package ru.zarina.zarina.data.category.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.category.remote.api.dto.CategoryResponseDto
import ru.zarina.zarina.di.Authorization
import javax.inject.Inject

class KtorZarinaCategoryApi @Inject constructor(
    @Authorization(Authorization.Type.TOKEN)
    private val client: HttpClient,
) : IZarinaCategoryApi {

    override suspend fun getCategories(): CategoryResponseDto {
        val response = client.get("/api/categories")
        return response.body()
    }

}
