package ru.livetyping.zarina.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.livetyping.zarina.presentation.activity.lifecycleobserver.ActivityResultRegistryHolderLifecycleObserver
import ru.livetyping.zarina.presentation.activity.lifecycleobserver.PermissionManagerActivityLifecycleObserver
import ru.livetyping.zarina.presentation.activity.lifecycleobserver.SmsCodeRetrieverActivityLifecycleObserver
import ru.livetyping.zarina.presentation.base.activity.lifecycleobserver.ActivityLifecycleObserver

@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityLifecycleObserverModule {

    @Binds
    @IntoSet
    abstract fun bindPermissionManagerActivityLifecycleObserver(
        impl: PermissionManagerActivityLifecycleObserver,
    ): ActivityLifecycleObserver

    @Binds
    @IntoSet
    abstract fun bindSmsCodeRetrieverActivityLifecycleObserver(
        impl: SmsCodeRetrieverActivityLifecycleObserver,
    ): ActivityLifecycleObserver

    @Binds
    @IntoSet
    abstract fun bindActivityResultRegistryHolderLifecycleObserver(
        impl: ActivityResultRegistryHolderLifecycleObserver,
    ): ActivityLifecycleObserver
}
