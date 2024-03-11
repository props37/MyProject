package ru.zarina.zarina.usecase.rework.content

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.usecase.FlowUseCase
import ru.zarina.zarina.data.content.ContentRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.content.HomeContent
import javax.inject.Inject

class GetHomeContentFlowUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val contentRepository: ContentRepository,
) : FlowUseCase<Unit, HomeContent>(dispatcher) {

    override fun execute(params: Unit): Flow<HomeContent> {
        return contentRepository.getHomeContentFlow()
    }
}
