package ru.zarina.zarina.data.subscription.remote

interface ISubscriptionRemoteSource {
    suspend fun subscribe(offerBarcode: String, name: String, email: String)
}
