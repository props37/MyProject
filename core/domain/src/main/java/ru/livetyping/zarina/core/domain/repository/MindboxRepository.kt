package ru.livetyping.zarina.core.domain.repository

import ru.livetyping.zarina.core.domain.model.user.User

public interface MindboxRepository {
    public suspend fun onUserSignedUp(user: User)

    public suspend fun onUserSignedIn(user: User)
}
