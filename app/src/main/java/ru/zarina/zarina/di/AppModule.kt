package ru.zarina.zarina.di

import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

val appModule = module {
    includes(
        defaultModule,
        CoroutineModule().module,
        DatabaseModule().module,
        DataStoreModule().module,
        NetworkModule().module,
        PlayerModule().module,
        PlayServicesModule().module,
        viewModelModule,
    )
}
