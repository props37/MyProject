package ru.zarina.zarina.data.rework.user.local

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.zarina.zarina.data.rework.user.local.entity.CityEntity
import ru.zarina.zarina.domain.rework.geography.City
import timber.log.Timber
import javax.inject.Inject

class UserCityDataHolder @Inject constructor(
    private val dataStore: DataStore<CityEntity?>,
) {
    fun getUserCity(): Flow<City?> {
        return dataStore.data.map { it?.toCity() }
    }

    suspend fun setUserCity(city: City?) {
        Timber.v("Set user city: $city")
        dataStore.updateData {
            city?.let { CityEntity.from(it) }
        }
    }

    suspend fun clear() {
        Timber.v("Clear user city")
        setUserCity(null)
    }
}
