package ru.livetyping.zarina.core.domain.usecase.geo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.core.domain.model.geo.Street
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.domain.repository.UserRepository
import ru.livetyping.zarina.core.domain.usecase.geo.GetCityStreetsFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCityStreetsFlowUseCaseImpl(
    private val geographyRepository: GeographyRepository,
    private val userRepository: UserRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<Street>>(logger), GetCityStreetsFlowUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Params): Flow<List<Street>> {
        if (params.nameQuery.isBlank()) {
            return flow { throw EmptySearchQueryException() }
        }

        return userRepository.getUserCityFlow(CachePolicy.LocalOnly).flatMapLatest { city ->
            checkNotNull(city) { "city is null" }
            geographyRepository.getCityStreetsFlow(city.id, params.nameQuery)
        }
    }

    override fun invoke(params: Params): Flow<Result<List<Street>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCityStreetsFlowUseCaseImpl"
    }
}
