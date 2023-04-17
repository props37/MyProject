package ru.zarina.zarina.usecase.content

import kotlinx.coroutines.CoroutineDispatcher
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.content.ContentRepository
import ru.zarina.zarina.di.Dispatcher
import ru.zarina.zarina.di.ZarinaDispatcher
import ru.zarina.zarina.domain.Url
import javax.inject.Inject

class GetOnboardingSplashUseCase @Inject constructor(
    @Dispatcher(ZarinaDispatcher.IO) dispatcher: CoroutineDispatcher,
    private val contentRepository: ContentRepository,
) : UseCase<Unit, Url?>(dispatcher) {
    override suspend fun execute(params: Unit): Url? = contentRepository.getOnboardingSplash()
}
