package ru.zarina.zarina.data.subscription

import ru.zarina.zarina.domain.Barcode

interface ISubscriptionRepository {
    suspend fun subscribe(offerBarcode: Barcode, name: String, email: String)
}
