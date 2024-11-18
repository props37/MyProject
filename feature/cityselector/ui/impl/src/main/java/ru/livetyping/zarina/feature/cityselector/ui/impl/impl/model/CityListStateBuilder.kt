package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

internal class CityListStateBuilder {
    fun build(
        citiesForNameQueryResult: Result<CitiesForNameQuery>,
        citiesLoadingState: FlowRequester.LoadingState,
        selectedCity: City?,
        mainCityKladrIds: List<KladrId>,
        isChangeCityButtonVisible: Boolean,
    ): CityListState {
        return if (!citiesLoadingState.isLoading()) {
            citiesForNameQueryResult.fold(
                onSuccess = { citiesForNameQuery ->
                    val cityNameQuery = citiesForNameQuery.cityNameQuery
                    val cities = citiesForNameQuery.cities
                    if (cities.isNotEmpty()) {
                        if (cityNameQuery.isBlank()) {
                            getSuccessStateWithCityListGroupedByFirstLetter(
                                cities = cities,
                                selectedCity = selectedCity,
                                mainCityKladrIds = mainCityKladrIds,
                                isChangeCityButtonVisible = isChangeCityButtonVisible,
                            )
                        } else {
                            getSuccessStateWithPlainCityList(
                                cities = cities,
                                selectedCity = selectedCity,
                                isChangeCityButtonVisible = isChangeCityButtonVisible,
                            )
                        }
                    } else {
                        CityListState.Empty
                    }
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    CityListState.Error(errorState)
                },
            )
        } else {
            CityListState.Loading
        }
    }

    private fun getSuccessStateWithCityListGroupedByFirstLetter(
        cities: List<City>,
        selectedCity: City?,
        mainCityKladrIds: List<KladrId>,
        isChangeCityButtonVisible: Boolean,
    ): CityListState.Success {
        // Show main cities at the top
        val items = buildList {
            val (mainCities, otherCities) = cities.partition { city ->
                city.id in mainCityKladrIds
            }
            val mainCityItems = mainCities.map { CityListItem.CityItem(it) }
            addAll(mainCityItems)

            // Show other cities grouped by the first letter
            val otherCitiesGrouped = otherCities.groupBy { city ->
                city.name.firstOrNull()
            }
            otherCitiesGrouped.forEach { (firstLetter, cities) ->
                if (firstLetter != null) {
                    add(CityListItem.CityFirstLetterHeaderItem(firstLetter))
                }
                val cityItems = cities.map { CityListItem.CityItem(it) }
                addAll(cityItems)
            }
        }.toImmutableList()
        return CityListState.Success(items, selectedCity, isChangeCityButtonVisible)
    }

    private fun getSuccessStateWithPlainCityList(
        cities: List<City>,
        selectedCity: City?,
        isChangeCityButtonVisible: Boolean,
    ): CityListState.Success {
        val items = cities
            .map { city ->
                CityListItem.CityItem(city, showFullName = true)
            }
            .toImmutableList()
        return CityListState.Success(items, selectedCity, isChangeCityButtonVisible)
    }
}
