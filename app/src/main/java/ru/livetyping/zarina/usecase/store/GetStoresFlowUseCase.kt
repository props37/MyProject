package ru.livetyping.zarina.usecase.store

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.store.StoreRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.store.Store
import javax.inject.Inject

class GetStoresFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val storeRepository: StoreRepository,
) : FlowUseCase<Unit, List<Store>>(dispatcher) {

    override fun execute(params: Unit): Flow<List<Store>> {
        return storeRepository.getStoresFlow()
    }
}
