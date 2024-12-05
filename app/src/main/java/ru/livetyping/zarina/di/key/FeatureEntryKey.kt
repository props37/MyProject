package ru.livetyping.zarina.di.key

import dagger.MapKey
import ru.livetyping.zarina.core.feature.FeatureEntry
import kotlin.reflect.KClass

@MapKey
internal annotation class FeatureEntryKey(val value: KClass<out FeatureEntry<*, *, *>>)
