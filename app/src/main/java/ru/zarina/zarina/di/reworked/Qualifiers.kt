package ru.zarina.zarina.di.reworked

import javax.inject.Qualifier

object Qualifiers {
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class CoroutineDispatcher(val dispatcher: CoroutineDispatchers)

    enum class CoroutineDispatchers { MAIN, MAIN_IMMEDIATE, IO, DEFAULT }
}
