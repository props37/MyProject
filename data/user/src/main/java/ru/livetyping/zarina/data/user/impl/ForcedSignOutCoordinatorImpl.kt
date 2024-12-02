package ru.livetyping.zarina.data.user.impl

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.livetyping.zarina.core.domain.manager.ForcedSignOutCoordinator
import timber.log.Timber
import javax.inject.Inject

internal class ForcedSignOutCoordinatorImpl @Inject constructor() : ForcedSignOutCoordinator {
    private val requests = Channel<Unit>(Channel.CONFLATED)

    override fun getForcedSignOutRequests(): Flow<Unit> {
        return requests.receiveAsFlow()
    }

    override fun requestForcedSignOut() {
        requests.trySend(Unit)
        Timber.tag(TAG).v("Forced sign out requested")
    }

    private companion object {
        private const val TAG = "ForcedSignOutCoordinator"
    }
}
