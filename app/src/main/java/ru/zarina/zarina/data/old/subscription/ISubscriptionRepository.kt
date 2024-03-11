package ru.zarina.zarina.data.old.subscription

import ru.zarina.zarina.domain.old.Barcode

interface ISubscriptionRepository {
    suspend fun subscribe(offerBarcode: Barcode, name: String, email: String)
}
