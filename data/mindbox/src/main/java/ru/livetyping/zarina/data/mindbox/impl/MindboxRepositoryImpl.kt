package ru.livetyping.zarina.data.mindbox.impl

import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.repository.MindboxRepository
import ru.livetyping.zarina.data.mindbox.impl.api.MindboxApi
import javax.inject.Inject

internal class MindboxRepositoryImpl @Inject constructor(
    private val api: MindboxApi,
) : MindboxRepository {
    override suspend fun onUserSignedUp(user: User) {
        api.onUserSignedUp(user)
    }

    override suspend fun onUserSignedIn(user: User) {
        api.onUserSignedIn(user)
    }
}
