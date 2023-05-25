package ru.zarina.zarina.data.subscription.remote.api

import ru.zarina.zarina.data.subscription.remote.api.dto.SubscribeRequestBody

interface IZarinaSubscriptionApi {
    suspend fun subscribe(body: SubscribeRequestBody)
}

