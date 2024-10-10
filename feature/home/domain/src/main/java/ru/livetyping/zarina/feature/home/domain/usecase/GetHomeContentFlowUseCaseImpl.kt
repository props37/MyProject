package ru.livetyping.zarina.feature.home.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.usecase.FlowUseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.repository.HomeContentRepository

internal class GetHomeContentFlowUseCaseImpl(
    private val homeContentRepository: HomeContentRepository,
    logger: UseCaseLogger?,
) : FlowUseCase<Unit, HomeContent>(logger), GetHomeContentFlowUseCase {
    override fun execute(params: Unit): Flow<HomeContent> {
        return homeContentRepository.getHomeContentFlow()
    }

    override fun invoke(): Flow<Result<HomeContent>> {
        return invoke(Unit)
    }
}
