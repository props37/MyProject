package ru.zarina.zarina.usecase.rework.content

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.data.rework.content.ContentRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.content.HomeContent
import ru.zarina.zarina.usecase.base.FlowUseCase
import javax.inject.Inject

class GetHomeContentUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val contentRepository: ContentRepository,
) : FlowUseCase<Unit, HomeContent>(dispatcher) {

    override fun execute(params: Unit): Flow<HomeContent> {
        return contentRepository.getHomeContent()
    }
}
