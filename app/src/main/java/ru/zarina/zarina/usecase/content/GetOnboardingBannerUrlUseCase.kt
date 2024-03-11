package ru.zarina.zarina.usecase.content

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.usecase.UseCase
import ru.zarina.zarina.data.content.ContentRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.rework.common.Url
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
