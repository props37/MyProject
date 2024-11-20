package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.user.User

@Stable
internal sealed class ProfileUserState {
    @Immutable
    data class Success(val user: User?) : ProfileUserState()

    data object Loading : ProfileUserState()
}
