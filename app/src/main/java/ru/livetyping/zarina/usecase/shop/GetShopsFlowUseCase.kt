package ru.livetyping.zarina.usecase.shop

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.shop.ShopRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.shop.Shop
import javax.inject.Inject

class GetShopsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val shopRepository: ShopRepository,
) : FlowUseCase<Unit, List<Shop>>(dispatcher) {

    override fun execute(params: Unit): Flow<List<Shop>> {
        return shopRepository.getShopsFlow()
    }
}
