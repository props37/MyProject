package ru.zarina.zarina.di

import org.junit.Test
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class ValidationTest : KoinTest {

    @Test
    fun validateModules() {
        checkModules {
            modules(
                defaultModule,
                CoroutineModule().module,
                DatabaseModule().module,
                DataStoreModule().module,
                NetworkModule().module,
                PlayerModule().module,
                PlayServicesModule().module
            )
        }
    }

}
