package ru.zarina.zarina.usecase.rework.home

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.content.ContentRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.content.HomeBanners
import ru.zarina.zarina.usecase.base.FlowUseCase
import javax.inject.Inject

class GetHomeBannersUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val contentRepository: ContentRepository,
) : FlowUseCase<Unit, HomeBanners>(dispatcher) {

    override fun execute(params: Unit): Flow<HomeBanners> {
        return contentRepository.getHomeBanners()
    }
}
