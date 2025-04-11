package ru.livetyping.zarina.feature.home.domain.usecase

import ru.livetyping.zarina.core.usecase.UseCase
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.repository.HomeContentRepository

internal class GetHomeContentUseCaseImpl(
    private val homeContentRepository: HomeContentRepository,
    logger: UseCaseLogger?,
) : UseCase<Unit, HomeContent>(logger), GetHomeContentUseCase {

    override suspend fun execute(params: Unit): HomeContent {
        return homeContentRepository.getHomeContent()
    }

    override suspend fun invoke(): Result<HomeContent> {
        return call(Unit)
    }
}
