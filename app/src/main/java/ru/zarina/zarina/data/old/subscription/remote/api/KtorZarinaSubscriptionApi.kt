package ru.zarina.zarina.data.old.subscription.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.subscription.remote.api.dto.SubscribeRequestBody
import ru.zarina.zarina.di.old.Qualifiers

@Factory
class KtorZarinaSubscriptionApi(
    @Named(Qualifiers.Api.ZARINA_RESTRICTED)
    private val client: HttpClient,
) : IZarinaSubscriptionApi {

    override suspend fun subscribe(body: SubscribeRequestBody) {
        client.post("/api/subscriptions/subscribe") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

}
