package ru.zarina.zarina.usecase.rework.home

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.home.HomeRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.home.HomeBanners
import ru.zarina.zarina.usecase.base.FlowUseCase
import javax.inject.Inject

class GetHomeBannersUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository,
) : FlowUseCase<Unit, HomeBanners>(dispatcher) {

    override fun execute(params: Unit): Flow<HomeBanners> {
        return homeRepository.getBanners()
    }
}
