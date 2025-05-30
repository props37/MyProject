package ru.livetyping.zarina.core.domain.usecase.geo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.livetyping.zarina.core.domain.model.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.domain.usecase.geo.GetStreetBuildingsFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetStreetBuildingsFlowUseCaseImpl(
    private val geographyRepository: GeographyRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<Building>>(logger), GetStreetBuildingsFlowUseCase {

    override fun execute(params: Params): Flow<List<Building>> {
        if (params.nameQuery.isBlank()) {
            return flow { throw EmptySearchQueryException() }
        }

        return geographyRepository.getStreetBuildings(params.streetFiasId, params.nameQuery)
    }

    override fun invoke(params: Params): Flow<Result<List<Building>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetStreetBuildingsFlowUseCaseImpl"
    }
}
