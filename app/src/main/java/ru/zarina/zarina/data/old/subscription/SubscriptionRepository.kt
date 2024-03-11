package ru.zarina.zarina.data.old.subscription

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.subscription.remote.ISubscriptionRemoteSource
import ru.zarina.zarina.domain.Barcode

@Factory
class SubscriptionRepository(
    private val remote: ISubscriptionRemoteSource,
) : ISubscriptionRepository {

    override suspend fun subscribe(offerBarcode: Barcode, name: String, email: String) {
        remote.subscribe(offerBarcode, name, email)
    }
}
