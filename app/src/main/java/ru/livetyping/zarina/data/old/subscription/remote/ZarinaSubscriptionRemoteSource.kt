package ru.livetyping.zarina.data.old.subscription.remote

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.subscription.remote.api.IZarinaSubscriptionApi
import ru.livetyping.zarina.data.old.subscription.remote.api.dto.SubscribeRequestBody
import ru.livetyping.zarina.domain.old.Barcode

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
