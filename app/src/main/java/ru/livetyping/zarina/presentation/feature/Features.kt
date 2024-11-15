package ru.livetyping.zarina.presentation.feature

import ru.livetyping.zarina.core.feature.FeatureEntry

typealias Features = Map<Class<out FeatureEntry<*, *>>, @JvmSuppressWildcards FeatureEntry<*, *>>
