package ru.livetyping.zarina.data.signout

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForcedSignOutCoordinator @Inject constructor() {
    private val _forcedSignOutRequests = Channel<Unit>(Channel.CONFLATED)
    val forcedSignOutRequests: Flow<Unit> = _forcedSignOutRequests.receiveAsFlow()

    fun requestForcedSignOut() {
        Timber.v("Forced sign out requested")
        _forcedSignOutRequests.trySend(Unit)
    }
}
