package ru.zarina.zarina.usecase.old.content

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.content.IContentRepository
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.old.Banner
import ru.zarina.zarina.base.usecase.UseCase
import timber.log.Timber

@Factory
class GetBannersUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val contentRepository: IContentRepository,
) : UseCase<Unit, List<Banner>>(dispatcher) {
    override suspend fun execute(params: Unit): List<Banner> {
        val banners = contentRepository.getBanners()
        Timber.v("Loaded ${banners.size} banners")
        return banners
    }
}
