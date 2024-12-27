package ru.livetyping.zarina.core.domain.usecase.store

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.repository.StoreRepository
import ru.livetyping.zarina.core.usecase.UseCaseLogger

public interface GetStoresFlowUseCase {
    public operator fun invoke(params: Params): Flow<Result<List<Store>>>

    public data class Params(val cachePolicy: CachePolicy)

    public companion object {
        public fun getInstance(
            storeRepository: StoreRepository,
            logger: UseCaseLogger?,
        ): GetStoresFlowUseCase {
            return GetStoresFlowUseCaseImpl(
                storeRepository = storeRepository,
                logger = logger,
            )
        }
    }
}
