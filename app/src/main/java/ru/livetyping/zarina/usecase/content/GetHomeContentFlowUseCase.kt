package ru.livetyping.zarina.usecase.content

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.base.usecase.FlowUseCase
import ru.livetyping.zarina.data.content.ContentRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.content.HomeContent
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
