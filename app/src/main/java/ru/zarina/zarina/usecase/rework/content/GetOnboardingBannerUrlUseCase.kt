package ru.zarina.zarina.usecase.rework.content

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.data.rework.content.ContentRepository
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.usecase.base.UseCase
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
