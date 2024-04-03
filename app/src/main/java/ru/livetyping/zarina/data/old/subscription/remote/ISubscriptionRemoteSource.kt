package ru.livetyping.zarina.data.old.subscription.remote

import ru.livetyping.zarina.domain.old.Barcode

interface ISubscriptionRemoteSource {
    suspend fun subscribe(offerBarcode: Barcode, name: String, email: String)
}
