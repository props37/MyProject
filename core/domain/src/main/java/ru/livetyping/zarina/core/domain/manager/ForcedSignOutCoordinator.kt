package ru.livetyping.zarina.core.domain.manager

import kotlinx.coroutines.flow.Flow

public interface ForcedSignOutCoordinator {
    public fun getForcedSignOutRequests(): Flow<Unit>

    public fun requestForcedSignOut()
}
