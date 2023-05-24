package ru.zarina.zarina.data.subscription

interface ISubscriptionRepository {
    suspend fun subscribe(offerBarcode: String, name: String, email: String)
}
