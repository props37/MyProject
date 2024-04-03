package ru.livetyping.zarina.data.old.category.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.data.old.category.remote.api.dto.CategoryResponseDto
import ru.livetyping.zarina.di.old.Qualifiers

@Factory
class KtorZarinaCategoryApi(
    @Named(Qualifiers.Api.ZARINA_RESTRICTED)
    private val client: HttpClient,
) : IZarinaCategoryApi {

    override suspend fun getCategories(): CategoryResponseDto {
        val response = client.get("/api/categories")
        return response.body()
    }

}
