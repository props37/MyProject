package ru.livetyping.zarina.core.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetUserCityFlowUseCaseImpl(
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, City?>(logger), GetUserCityFlowUseCase {

    override fun execute(params: Params): Flow<City?> {
        return userRepository.getUserCityFlow(params.cachePolicy)
    }

    override fun invoke(params: Params): Flow<Result<City?>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetUserCityFlowUseCaseImpl"
    }
}
