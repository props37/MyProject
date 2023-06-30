package ru.zarina.zarina.data.subscription.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.subscription.remote.api.IZarinaSubscriptionApi
import ru.zarina.zarina.data.subscription.remote.api.dto.SubscribeRequestBody
import ru.zarina.zarina.domain.Barcode

@Factory
class ZarinaSubscriptionRemoteSource(
    private val api: IZarinaSubscriptionApi,
) : ISubscriptionRemoteSource {

    override suspend fun subscribe(offerBarcode: Barcode, name: String, email: String) {
        val body = SubscribeRequestBody(
            barcode = offerBarcode.value,
            firstName = name,
            mail = email,
        )
        api.subscribe(body)
    }
}
