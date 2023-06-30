package ru.zarina.zarina.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ValidationTest : KoinTest {

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val application = mock(Application::class.java)
    private val context = mock(Context::class.java)
    private val savedStateHandle = mock(SavedStateHandle::class.java)

    init {
        `when`(application.applicationContext)
            .thenReturn(context)
        `when`(context.applicationContext)
            .thenReturn(context)
        `when`(savedStateHandle.getStateFlow<String>(anyString(), anyString()))
            .thenReturn(MutableStateFlow(""))
    }

    private val mockModule = module {
        single { savedStateHandle }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun validateModules() {
        checkModules {
            androidContext(application)

            modules(
                mockModule,
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
