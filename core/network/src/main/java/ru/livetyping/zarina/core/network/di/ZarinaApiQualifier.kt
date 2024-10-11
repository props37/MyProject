package ru.livetyping.zarina.core.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
public annotation class ZarinaApiQualifier(val api: ZarinaApi)

public enum class ZarinaApi { AUTHORIZED, UNAUTHORIZED }
