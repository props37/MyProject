package ru.livetyping.zarina.core.domain.usecase.geo

import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.repository.GeographyRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCitiesUseCase {
    public suspend operator fun invoke(params: Params): Result<List<City>>

    public data class Params(
        val nameQuery: String?,
        val cachePolicy: CachePolicy,
    )

    public companion object {
        public fun getInstance(
            geographyRepository: GeographyRepository,
            logger: UseCaseLogger?,
        ): GetCitiesUseCase {
            return GetCitiesUseCaseImpl(
                geographyRepository = geographyRepository,
                logger = logger,
            )
        }
    }
}
