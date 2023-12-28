package ru.zarina.zarina.usecase.content

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.content.IContentRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.usecase.base.UseCase
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
