package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.manager.ForcedSignOutCoordinator
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetForcedSignOutRequestFlowUseCaseImpl(
    private val forcedSignOutCoordinator: ForcedSignOutCoordinator,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, Unit>(logger), GetForcedSignOutRequestFlowUseCase {

    override fun execute(params: Unit): Flow<Unit> {
        return forcedSignOutCoordinator.getForcedSignOutRequests()
    }

    override fun invoke(): Flow<Result<Unit>> {
        return call(Unit)
    }

    private companion object {
        private const val TAG = "GetForcedSignOutRequestFlowUseCaseImpl"
    }
}
