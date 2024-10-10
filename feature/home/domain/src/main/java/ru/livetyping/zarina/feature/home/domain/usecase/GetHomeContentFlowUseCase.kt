package ru.livetyping.zarina.feature.home.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.usecase.UseCaseLogger
import ru.livetyping.zarina.feature.home.domain.model.HomeContent
import ru.livetyping.zarina.feature.home.domain.repository.HomeContentRepository

public interface GetHomeContentFlowUseCase {
    public operator fun invoke(): Flow<Result<HomeContent>>

    public companion object {
        public fun getInstance(
            homeContentRepository: HomeContentRepository,
            logger: UseCaseLogger?,
        ): GetHomeContentFlowUseCase {
            return GetHomeContentFlowUseCaseImpl(homeContentRepository, logger)
        }
    }
}
