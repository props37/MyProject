package ru.livetyping.zarina.data.content.impl.local.gender

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.core.datastore.safeData
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.kotlinutil.enumValueOfOrNull
import timber.log.Timber
import javax.inject.Inject

internal class ContentGenderDataHolderImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
) : ContentGenderDataHolder {
    override fun getLastContentGenderFlow(): Flow<Gender?> {
        return preferencesDataStore.safeData
            .map { data ->
                val name = data[KEY_LAST_CONTENT_GENDER]
                val gender = name?.let { enumValueOfOrNull<Gender>(it) }
                gender
            }
            .distinctUntilChanged()
            .onEach { gender ->
                Timber.tag(TAG).v("Last content gender: $gender")
            }
    }

    override suspend fun setLastContentGender(gender: Gender) {
        preferencesDataStore.edit { data ->
            data[KEY_LAST_CONTENT_GENDER] = gender.name
        }
        Timber.tag(TAG).v("Content gender set: $gender")
    }

    override suspend fun clear() {
        preferencesDataStore.edit { data ->
            data.remove(KEY_LAST_CONTENT_GENDER)
        }
        Timber.tag(TAG).v("Content gender cleared")
    }

    companion object {
        private val KEY_LAST_CONTENT_GENDER = stringPreferencesKey("last_content_gender")

        private const val TAG = "ContentGenderDataHolderImpl"
    }
}
