package ru.livetyping.zarina.feature.home.domain.usecase

import ru.livetyping.zarina.core.usecase.UseCaseLogger
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.repository.HomeContentRepository

public interface GetHomeContentUseCase {
    public suspend operator fun invoke(): Result<HomeContent>

    public companion object {
        public fun getInstance(
            homeContentRepository: HomeContentRepository,
            logger: UseCaseLogger?,
        ): GetHomeContentUseCase {
            return GetHomeContentUseCaseImpl(homeContentRepository, logger)
        }
    }
}
