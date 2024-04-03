package ru.livetyping.zarina.data.user.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.util.kotlin.enumValueOfOrNull
import ru.livetyping.zarina.util.library.datastore.safeData
import timber.log.Timber
import javax.inject.Inject

class UserContentGenderDataHolder @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
) {
    fun getUserContentGenderFlow(): Flow<Gender?> {
        return preferencesDataStore.safeData.map { data ->
            val name = data[KEY_USER_CONTENT_GENDER]
            name?.let { enumValueOfOrNull<Gender>(it) }
        }
    }

    suspend fun setUserContentGender(gender: Gender) {
        Timber.v("Set user content gender: $gender")
        preferencesDataStore.edit { data ->
            data[KEY_USER_CONTENT_GENDER] = gender.name
        }
    }

    companion object {
        private val KEY_USER_CONTENT_GENDER = stringPreferencesKey("user_content_gender")
    }
}
