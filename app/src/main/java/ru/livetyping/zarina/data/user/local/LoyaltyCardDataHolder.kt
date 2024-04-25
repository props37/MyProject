package ru.livetyping.zarina.data.user.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.livetyping.zarina.domain.user.LoyaltyCard
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoyaltyCardDataHolder @Inject constructor() {
    private val loyaltyCard = MutableStateFlow<LoyaltyCard?>(null)

    fun getLoyaltyCardFlow(): Flow<LoyaltyCard?> {
        return loyaltyCard
    }

    fun setLoyaltyCard(card: LoyaltyCard) {
        loyaltyCard.value = card
    }

    fun clear() {
        loyaltyCard.value = null
    }
}
