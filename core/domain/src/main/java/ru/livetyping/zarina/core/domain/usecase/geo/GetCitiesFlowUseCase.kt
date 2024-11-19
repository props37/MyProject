package ru.livetyping.zarina.core.domain.usecase.geo

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCitiesFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<City>>>

    public data class Params(
        val nameQuery: String?,
    )

    public companion object {
        public fun getInstance(
            geographyRepository: GeographyRepository,
            logger: UseCaseLogger?,
        ): GetCitiesFlowUseCase {
            return GetCitiesFlowUseCaseImpl(
                geographyRepository = geographyRepository,
                logger = logger,
            )
        }
    }
}
