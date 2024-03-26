package ru.zarina.zarina.data.old.subscription.remote.api

import ru.zarina.zarina.data.old.subscription.remote.api.dto.SubscribeRequestBody

interface IZarinaSubscriptionApi {
    suspend fun subscribe(body: SubscribeRequestBody)
}

