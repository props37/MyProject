package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.manager.ForcedSignOutCoordinator
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetForcedSignOutRequestsFlowUseCase {
    public operator fun invoke(): Flow<Result<Unit>>

    public companion object {
        public fun getInstance(
            forcedSignOutCoordinator: ForcedSignOutCoordinator,
            logger: UseCaseLogger?,
        ): GetForcedSignOutRequestsFlowUseCase {
            return GetForcedSignOutRequestsFlowUseCaseImpl(
                forcedSignOutCoordinator = forcedSignOutCoordinator,
                logger = logger,
            )
        }
    }
}
