package ru.livetyping.zarina.usecase.shop

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.shop.ShopRepository
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.shop.Shop
import javax.inject.Inject

class GetUserCityShopsFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository,
) : FlowUseCase<Unit, List<Shop>>(dispatcher) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(params: Unit): Flow<List<Shop>> {
        return userRepository.getUserCityFlow()
            .flatMapLatest { city ->
                checkNotNull(city) { "User city is null" }
                shopRepository.getShopsFlow()
                    .map { shops ->
                        shops.filter { shop ->
                            shop.cityKladrId == city.kladrId
                        }
                    }
            }
    }
}
