package ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class LoyaltyProgramState {
    @Immutable
    data class Success(val loyaltyCard: LoyaltyCard) : LoyaltyProgramState()

    data object Loading : LoyaltyProgramState()

    data class Error(val state: ZarinaErrorScreenState) : LoyaltyProgramState()
}
