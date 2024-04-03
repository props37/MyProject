package ru.livetyping.zarina.data.old.subscription.remote.api

import ru.livetyping.zarina.data.old.subscription.remote.api.dto.SubscribeRequestBody

interface IZarinaSubscriptionApi {
    suspend fun subscribe(body: SubscribeRequestBody)
}

