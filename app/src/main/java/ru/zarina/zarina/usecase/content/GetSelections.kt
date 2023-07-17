package ru.zarina.zarina.usecase.content

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.base.clean.UseCase
import ru.zarina.zarina.data.content.IContentRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.Selection
import timber.log.Timber

@Factory
class GetSelectionsUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val contentRepository: IContentRepository,
) : UseCase<Unit, List<Selection>>(dispatcher) {
    override suspend fun execute(params: Unit): List<Selection> {
        val selections = contentRepository.getSelections()
        Timber.v("Loaded ${selections.size} selections")
        return selections
    }
}