package ru.livetyping.zarina.core.domain.usecase.catalog

import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetCatalogMenuUseCase {
    public suspend operator fun invoke(params: Params): Result<CatalogMenuByGender>

    public data class Params(val cachePolicy: CachePolicy)

    public companion object {
        public fun getInstance(
            contentRepository: ContentRepository,
            logger: UseCaseLogger?,
        ): GetCatalogMenuUseCase {
            return GetCatalogMenuUseCaseImpl(
                contentRepository = contentRepository,
                logger = logger,
            )
        }
    }
}
