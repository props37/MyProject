package ru.livetyping.zarina.data.old.subscription

import ru.livetyping.zarina.domain.old.Barcode

interface ISubscriptionRepository {
    suspend fun subscribe(offerBarcode: Barcode, name: String, email: String)
}
