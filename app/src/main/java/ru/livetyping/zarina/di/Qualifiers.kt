package ru.livetyping.zarina.di

import javax.inject.Qualifier

object Qualifiers {
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ZarinaApi(val api: ZarinaApiType)

    enum class ZarinaApiType { AUTHORIZED, UNAUTHORIZED }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AnyQuery(val type: AnyQueryType)

    enum class AnyQueryType { AUTOCOMPLETE }


    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class CoroutineDispatcher(val dispatcher: CoroutineDispatchers)

    enum class CoroutineDispatchers { MAIN, MAIN_IMMEDIATE, IO, DEFAULT }


    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class SharedPreferences(val type: SharedPreferencesType)

    enum class SharedPreferencesType { ENCRYPTED }
}
