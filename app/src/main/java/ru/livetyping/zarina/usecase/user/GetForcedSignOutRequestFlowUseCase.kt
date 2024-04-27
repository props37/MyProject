package ru.livetyping.zarina.usecase.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.signout.ForcedSignOutCoordinator
import ru.livetyping.zarina.di.Qualifiers
import javax.inject.Inject

class GetForcedSignOutRequestFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val forcedSignOutCoordinator: ForcedSignOutCoordinator,
) : FlowUseCase<Unit, Unit>(dispatcher) {

    override fun execute(params: Unit): Flow<Unit> {
        return forcedSignOutCoordinator.forcedSignOutRequests
    }
}
