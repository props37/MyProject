package ru.livetyping.zarina.core.domain.usecase.geo

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.domain.usecase.geo.GetCitiesUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCitiesUseCaseImpl(
    private val geographyRepository: GeographyRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, List<City>>(logger), GetCitiesUseCase {

    override suspend fun execute(params: Params): List<City> {
        val nameQuery = params.nameQuery?.trim()?.takeIf { it.isNotBlank() }
        return geographyRepository.getCities(nameQuery, params.cachePolicy)
    }

    override suspend fun invoke(params: Params): Result<List<City>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCitiesFlowUseCaseImpl"
    }
}
