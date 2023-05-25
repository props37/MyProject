package ru.zarina.zarina.data.subscription.remote

import ru.zarina.zarina.domain.Barcode

interface ISubscriptionRemoteSource {
    suspend fun subscribe(offerBarcode: Barcode, name: String, email: String)
}
