package ru.livetyping.zarina.data.device.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.livetyping.zarina.util.library.datastore.safeData
import timber.log.Timber
import javax.inject.Inject

class OnboardingDataHolder @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
) {
    fun getIsOnboardingCompletedFlow(): Flow<Boolean> {
        return preferencesDataStore.safeData.map { data ->
            data[KEY_IS_ONBOARDING_COMPLETED] ?: false
        }
    }

    suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        Timber.v("Set onboarding completed: $isCompleted")
        preferencesDataStore.edit { data ->
            data[KEY_IS_ONBOARDING_COMPLETED] = isCompleted
        }
    }

    companion object {
        private val KEY_IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    }
}
