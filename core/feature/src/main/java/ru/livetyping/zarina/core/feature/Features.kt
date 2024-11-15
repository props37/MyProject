package ru.livetyping.zarina.core.feature

public typealias Features = Map<Class<out FeatureEntry<*, *>>, FeatureEntry<*, *>>

public inline fun <reified T : FeatureEntry<*, *>> Features.find(): T = this[T::class.java] as T
