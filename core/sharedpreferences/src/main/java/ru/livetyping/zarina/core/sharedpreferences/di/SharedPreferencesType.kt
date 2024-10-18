package ru.livetyping.zarina.core.sharedpreferences.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
public annotation class SharedPreferencesType(val type: SharedPrefType)

public enum class SharedPrefType { ENCRYPTED }
