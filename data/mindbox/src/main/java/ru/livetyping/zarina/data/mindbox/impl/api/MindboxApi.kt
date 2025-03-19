package ru.livetyping.zarina.data.mindbox.impl.api

import ru.livetyping.zarina.core.domain.model.user.User

internal interface MindboxApi {
    suspend fun onUserSignedUp(user: User)

    suspend fun onUserSignedIn(user: User)
}
