package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.livetyping.zarina.core.database.ZarinaDatabaseCallback
import ru.livetyping.zarina.core.domain.manager.ForcedSignOutCoordinator
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
class ZarinaDatabaseCallbackModule {

    @Provides
    fun provideZarinaDatabaseCallback(
        forcedSignOutCoordinator: ForcedSignOutCoordinator,
    ): ZarinaDatabaseCallback {
        return object : ZarinaDatabaseCallback {
            override fun onDestructiveMigration() {
                Timber.tag(TAG).w("ZarinaDatabase was destructively migrated")
                forcedSignOutCoordinator.requestForcedSignOut()
            }
        }
    }

    private companion object {
        private const val TAG = "ZarinaDatabaseCallback"
    }
}
