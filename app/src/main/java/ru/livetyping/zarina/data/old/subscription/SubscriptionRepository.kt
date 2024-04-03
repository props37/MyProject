package ru.livetyping.zarina.data.old.subscription

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.subscription.remote.ISubscriptionRemoteSource
import ru.livetyping.zarina.domain.old.Barcode

@Factory
class SubscriptionRepository(
    private val remote: ISubscriptionRemoteSource,
) : ISubscriptionRepository {

    override suspend fun subscribe(offerBarcode: Barcode, name: String, email: String) {
        remote.subscribe(offerBarcode, name, email)
    }
}
