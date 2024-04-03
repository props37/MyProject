package ru.livetyping.zarina.usecase.content

import kotlinx.coroutines.CoroutineDispatcher
import ru.livetyping.zarina.base.usecase.UseCase
import ru.livetyping.zarina.data.content.ContentRepository
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Url
import javax.inject.Inject

class GetOnboardingBannerUrlUseCase @Inject constructor(
    @Qualifiers.CoroutineDispatcher(Qualifiers.CoroutineDispatchers.IO)
    dispatcher: CoroutineDispatcher,
    private val contentRepository: ContentRepository,
) : UseCase<Unit, Url>(dispatcher) {

    override suspend fun execute(params: Unit): Url {
        return contentRepository.getOnboardingBannerUrl()
    }
}
