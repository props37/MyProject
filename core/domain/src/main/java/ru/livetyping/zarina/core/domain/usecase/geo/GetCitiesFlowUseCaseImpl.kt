package ru.livetyping.zarina.core.domain.usecase.geo

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.domain.usecase.geo.GetCitiesFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCitiesFlowUseCaseImpl(
    private val geographyRepository: GeographyRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<City>>(logger), GetCitiesFlowUseCase {

    override fun execute(params: Params): Flow<List<City>> {
        val nameQuery = params.nameQuery?.trim()?.takeIf { it.isNotBlank() }
        return geographyRepository.getCitiesFlow(nameQuery, params.cachePolicy)
    }

    override fun invoke(params: Params): Flow<Result<List<City>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCitiesFlowUseCaseImpl"
    }
}
