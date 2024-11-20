package ru.livetyping.zarina.data.user.impl.local.city

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.data.user.impl.local.city.entity.CityEntity
import timber.log.Timber
import javax.inject.Inject

internal class UserCityDataHolderImpl @Inject constructor(
    private val dataStore: DataStore<CityEntity?>,
) : UserCityDataHolder {
    override fun getUserCityFlow(): Flow<City?> {
        return dataStore.data.map { it?.toCity() }
    }

    override suspend fun setUserCity(city: City?) {
        dataStore.updateData {
            city?.let { CityEntity.from(it) }
        }
        Timber.tag(TAG).v("User city set: $city")
    }

    private companion object {
        private const val TAG = "UserCityDataHolderImpl"
    }
}
