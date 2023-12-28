package ru.zarina.zarina.usecase.geography

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.geography.IGeographyRepository
import ru.zarina.zarina.di.Qualifiers
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.usecase.base.UseCase
import timber.log.Timber

@Factory
class GetCitiesUseCase(
    @Named(Qualifiers.Dispatcher.IO) dispatcher: CoroutineDispatcher,
    private val geographyRepository: IGeographyRepository,
) : UseCase<GetCitiesUseCase.Params, List<City>>(dispatcher) {

    override suspend fun execute(params: Params): List<City> {
        val query = params.query?.trim()?.takeIf { it.isNotBlank() }

        val cities = geographyRepository.getCities(query)
        Timber.v("Found ${cities.size} cities with query \"$query\"")
        return cities
    }

    data class Params(
        val query: String?,
    )

}
