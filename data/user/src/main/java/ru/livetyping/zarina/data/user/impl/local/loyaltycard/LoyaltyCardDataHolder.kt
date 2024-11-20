package ru.livetyping.zarina.data.user.impl.local.loyaltycard

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard

internal interface LoyaltyCardDataHolder {
    fun getLoyaltyCardFlow(): Flow<LoyaltyCard?>

    fun setLoyaltyCard(card: LoyaltyCard?)
}
