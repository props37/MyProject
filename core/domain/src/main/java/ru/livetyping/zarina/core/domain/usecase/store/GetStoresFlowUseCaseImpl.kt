package ru.livetyping.zarina.core.domain.usecase.store

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.domain.repository.StoreRepository
import ru.livetyping.zarina.core.domain.usecase.store.GetStoresFlowUseCase.Params
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger

internal class GetStoresFlowUseCaseImpl(
    private val storeRepository: StoreRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Params, List<Store>>(logger), GetStoresFlowUseCase {

    override fun execute(params: Params): Flow<List<Store>> {
        return storeRepository.getStoresFlow(params.cachePolicy)
    }

    override fun invoke(params: Params): Flow<Result<List<Store>>> {
        return call(params)
    }

    private companion object {
        private const val TAG = "GetStoresFlowUseCaseImpl"
    }
}
