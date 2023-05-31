package ru.zarina.zarina.data.subscription.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.zarina.zarina.data.subscription.remote.api.dto.SubscribeRequestBody
import ru.zarina.zarina.di.Authorization
import javax.inject.Inject

class KtorZarinaSubscriptionApi @Inject constructor(
    @Authorization(Authorization.Type.TOKEN)
    private val client: HttpClient,
) : IZarinaSubscriptionApi {

    override suspend fun subscribe(body: SubscribeRequestBody) {
        client.post("/api/subscriptions/subscribe") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

}
