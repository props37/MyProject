package ru.livetyping.zarina.core.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
public annotation class DigineticaApi(val api: DigineticaApiType)

public enum class DigineticaApiType { AUTOCOMPLETE, REVIEW }
