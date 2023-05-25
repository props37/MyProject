package ru.zarina.zarina.data.subscription

import ru.zarina.zarina.data.subscription.remote.ISubscriptionRemoteSource
import ru.zarina.zarina.domain.Barcode
import javax.inject.Inject

class SubscriptionRepository @Inject constructor(
    private val remote: ISubscriptionRemoteSource,
) : ISubscriptionRepository {

    override suspend fun subscribe(offerBarcode: Barcode, name: String, email: String) {
        remote.subscribe(offerBarcode, name, email)
    }
}
