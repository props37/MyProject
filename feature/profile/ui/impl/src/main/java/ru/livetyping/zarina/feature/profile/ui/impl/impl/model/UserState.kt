package ru.livetyping.zarina.feature.profile.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.user.User

@Stable
internal sealed class UserState {
    @Immutable
    data class Success(val user: User?) : UserState()

    data object Loading : UserState()
}
