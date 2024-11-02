package ru.livetyping.zarina.data.onboarding.impl.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import timber.log.Timber
import javax.inject.Inject

internal class OnboardingDataHolderImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
) : OnboardingDataHolder {
    override suspend fun setIsOnboardingCompleted(isCompleted: Boolean) {
        preferencesDataStore.edit { data ->
            data[KEY_IS_ONBOARDING_COMPLETED] = isCompleted
        }
        Timber.tag(TAG).v("Onboarding completed set to $isCompleted")
    }

    private companion object {
        private val KEY_IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")

        private const val TAG = "OnboardingDataHolderImpl"
    }
}
