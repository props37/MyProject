package ru.zarina.zarina.data.subscription.remote

import ru.zarina.zarina.data.subscription.remote.api.IZarinaSubscriptionApi
import ru.zarina.zarina.data.subscription.remote.api.dto.SubscribeRequestBody
import javax.inject.Inject

class ZarinaSubscriptionRemoteSource @Inject constructor(
    private val api: IZarinaSubscriptionApi,
) : ISubscriptionRemoteSource {

    override suspend fun subscribe(offerBarcode: String, name: String, email: String) {
        val body = SubscribeRequestBody(
            barcode = offerBarcode,
            firstName = name,
            mail = email,
        )
        api.subscribe(body)
    }
}
