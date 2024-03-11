package ru.zarina.zarina.di

import androidx.lifecycle.SavedStateHandle
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import ru.zarina.zarina.di.old.appModule

class ValidationTest : KoinTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun validateModules() {
        appModule.verify(
            extraTypes = listOf(
                SavedStateHandle::class,
                // From here -- temporal workaround for Koin checking ALL class constructors for annotation declarations
                HttpClientEngine::class,
                HttpClientConfig::class
            )
        )
    }

}
