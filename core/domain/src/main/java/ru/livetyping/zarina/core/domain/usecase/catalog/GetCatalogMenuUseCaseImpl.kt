package ru.livetyping.zarina.core.domain.usecase.catalog

import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuByGender
import ru.livetyping.zarina.core.domain.repository.ContentRepository
import ru.livetyping.zarina.core.domain.usecase.catalog.GetCatalogMenuUseCase.Params
import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetCatalogMenuUseCaseImpl(
    private val contentRepository: ContentRepository,
    logger: UseCaseLogger?,
) : UseCase<Params, CatalogMenuByGender>(logger), GetCatalogMenuUseCase {

    override suspend fun execute(params: Params): CatalogMenuByGender {
        return contentRepository.getCatalogMenu(params.cachePolicy)
    }

    override suspend fun invoke(params: Params): Result<CatalogMenuByGender> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetCatalogMenuUseCaseImpl"
    }
}
