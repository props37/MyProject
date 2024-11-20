package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetUserFlowUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, User?>(logger), GetUserFlowUseCase {

    override fun execute(params: Params): Flow<User?> {
        return userRepository.getUserFlow(params.cachePolicy)
    }

    override fun invoke(params: Params): Flow<Result<User?>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetUserFlowUseCaseImpl"
    }
}
