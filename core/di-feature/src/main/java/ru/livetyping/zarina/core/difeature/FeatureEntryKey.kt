package ru.livetyping.zarina.core.difeature

import dagger.MapKey
import ru.livetyping.zarina.core.feature.FeatureEntry
import kotlin.reflect.KClass

@MapKey
public annotation class FeatureEntryKey(val value: KClass<out FeatureEntry<*, *>>)
