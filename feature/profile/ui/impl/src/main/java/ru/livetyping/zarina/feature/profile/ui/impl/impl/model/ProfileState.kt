package ru.livetyping.zarina.feature.profile.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard

@Immutable
internal data class ProfileState(
    val userState: UserState,
    val loyaltyCard: LoyaltyCard?,
    val userCity: City?,
    val menuItems: ImmutableList<MenuItem>,
    val versionDetails: ImmutableList<VersionDetails>,
)
