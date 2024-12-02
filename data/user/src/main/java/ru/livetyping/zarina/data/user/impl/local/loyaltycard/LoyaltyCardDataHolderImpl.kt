package ru.livetyping.zarina.data.user.impl.local.loyaltycard

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import timber.log.Timber
import javax.inject.Inject

internal class LoyaltyCardDataHolderImpl @Inject constructor() : LoyaltyCardDataHolder {
    private val loyaltyCard = MutableStateFlow<LoyaltyCard?>(null)

    override fun getLoyaltyCardFlow(): Flow<LoyaltyCard?> {
        return loyaltyCard
    }

    override fun setLoyaltyCard(card: LoyaltyCard?) {
        loyaltyCard.value = card
        Timber.tag(TAG).v("LoyaltyCard set: $card")
    }

    override fun clear() {
        loyaltyCard.value = null
        Timber.tag(TAG).v("LoyaltyCard cleared")
    }

    private companion object {
        private const val TAG = "LoyaltyCardDataHolderImpl"
    }
}
