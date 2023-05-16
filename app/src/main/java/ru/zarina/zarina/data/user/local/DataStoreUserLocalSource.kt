package ru.zarina.zarina.data.user.local

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.zarina.zarina.data.user.local.entity.CityDataEntity
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.utils.datastore.safeData
import javax.inject.Inject

class DataStoreUserLocalSource @Inject constructor(
    private val dataStore: DataStore<CityDataEntity?>,
) : IUserLocalSource {

    override suspend fun setCity(city: City?) {
        dataStore.updateData { city?.let { CityDataEntity.from(it) } }
    }

    override fun getCity(): Flow<City?> = dataStore.safeData.map { it?.toDomain() }

}
