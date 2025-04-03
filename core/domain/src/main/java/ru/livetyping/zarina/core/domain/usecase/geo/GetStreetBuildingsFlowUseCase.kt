package ru.livetyping.zarina.core.domain.usecase.geo

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetStreetBuildingsFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<Building>>>

    public data class Params(
        val streetKladrId: KladrId,
        val nameQuery: String,
    )

    public companion object {
        public fun getInstance(
            geographyRepository: GeographyRepository,
            logger: UseCaseLogger?,
        ): GetStreetBuildingsFlowUseCase {
            return GetStreetBuildingsFlowUseCaseImpl(
                geographyRepository = geographyRepository,
                logger = logger,
            )
        }
    }
}
