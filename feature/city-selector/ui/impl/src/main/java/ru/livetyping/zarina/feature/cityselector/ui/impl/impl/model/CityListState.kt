package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState2

@Stable
internal sealed class CityListState {
    @Immutable
    data class Success(
        val cities: ImmutableList<CityListItem>,
        val isSelectCityButtonVisible: Boolean,
        val isSelectCityButtonLoading: Boolean,
    ) : CityListState()

    data object CityNotFound : CityListState()

    data object Loading : CityListState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState2) : CityListState()

    class Builder {
        fun build(
            cityResult: Result<List<City>>?,
            isLoadingCities: Boolean,
            selectedCity: City?,
            priorityCityKladrIds: Set<KladrId>,
            isChangeCityButtonVisible: Boolean,
            isChangeCityButtonLoading: Boolean,
        ): CityListState {
            return if (isLoadingCities || cityResult == null) {
                Loading
            } else {
                cityResult.fold(
                    onSuccess = { cities ->
                        if (cities.isNotEmpty()) {
                            val (priorityCities, otherCities) = cities.partition { city ->
                                city.id in priorityCityKladrIds
                            }
                            val items = buildList {
                                val priorityItems = priorityCities.map { city ->
                                    CityListItem(city, isSelected = city == selectedCity)
                                }
                                addAll(priorityItems)

                                val otherItems = otherCities.map { city ->
                                    CityListItem(city, isSelected = city == selectedCity)
                                }
                                addAll(otherItems)
                            }.toImmutableList()

                            Success(
                                cities = items,
                                isSelectCityButtonVisible = isChangeCityButtonVisible,
                                isSelectCityButtonLoading = isChangeCityButtonLoading,
                            )
                        } else {
                            CityNotFound
                        }
                    },
                    onFailure = {
                        val errorState = ZarinaErrorScreenState2.from(it)
                        Error(errorState)
                    },
                )
            }
        }
    }
}
