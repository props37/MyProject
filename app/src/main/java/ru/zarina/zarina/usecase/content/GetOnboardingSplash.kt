package ru.zarina.zarina.usecase.content

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.content.IContentRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Url
import timber.log.Timber

@Factory
class GetOnboardingSplashUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val contentRepository: IContentRepository,
) : UseCase<Unit, Url>(dispatcher) {
    override suspend fun execute(params: Unit): Url {
        val url = checkNotNull(contentRepository.getOnboardingSplash())
        Timber.v("Onboarding splash url: $url")
        return url
    }
}
