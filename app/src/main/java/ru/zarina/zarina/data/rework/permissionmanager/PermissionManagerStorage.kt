package ru.zarina.zarina.data.rework.permissionmanager

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val PERMISSION_MANAGER_PREFERENCES_DATA_STORE_NAME = "permission_manager_data_store"

private val Context.permissionManagerPreferencesDataStore by preferencesDataStore(
    name = PERMISSION_MANAGER_PREFERENCES_DATA_STORE_NAME,
)

class PermissionManagerStorage(context: Context) {
    private val preferencesDataStore = context.permissionManagerPreferencesDataStore

    fun hasPermissionRequiredRequestRationale(permission: String): Flow<Boolean?> {
        return preferencesDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { data ->
                val key = createHasPermissionRequiredRequestRationaleKey(permission)
                data[key]
            }
    }

    fun haveMultiplePermissionsRequiredRequestRationale(
        permissions: List<String>,
    ): Flow<Map<String, Boolean?>> {
        return preferencesDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { data ->
                val map = mutableMapOf<String, Boolean?>()
                for (permission in permissions) {
                    val key = createHasPermissionRequiredRequestRationaleKey(permission)
                    map[permission] = data[key]
                }
                map
            }
    }

    suspend fun savePermissionRequiredRequestRationale(permission: String) {
        val key = createHasPermissionRequiredRequestRationaleKey(permission)
        preferencesDataStore.edit { data ->
            data[key] = true
        }
    }

    private fun createHasPermissionRequiredRequestRationaleKey(
        permission: String,
    ): Preferences.Key<Boolean> {
        val name = permission.lowercase() + HAS_PERMISSION_REQUIRED_REQUEST_RATIONALE_KEY_SUFFIX
        return booleanPreferencesKey(name)
    }

    private companion object {
        private const val HAS_PERMISSION_REQUIRED_REQUEST_RATIONALE_KEY_SUFFIX =
            "_has_required_request_rationale"
    }
}
