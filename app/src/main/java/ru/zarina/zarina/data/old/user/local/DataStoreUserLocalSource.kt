package ru.zarina.zarina.data.old.user.local

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.user.local.entity.CityDataEntity
import ru.zarina.zarina.di.old.Qualifiers
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.utils.datastore.safeData

@Factory
class DataStoreUserLocalSource(
    @Named(Qualifiers.DataStore.USER_CITY)
    private val dataStore: DataStore<CityDataEntity?>,
) : IUserLocalSource {

    override suspend fun setCity(city: City?) {
        dataStore.updateData { city?.let { CityDataEntity.from(it) } }
    }

    override fun getCity(): Flow<City?> = dataStore.safeData.map { it?.toDomain() }

}
