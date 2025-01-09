package ru.livetyping.zarina.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.livetyping.zarina.application.extension.BearerTokenFetcherApplicationExtension
import ru.livetyping.zarina.application.extension.CartProductIdsFetcherApplicationExtension
import ru.livetyping.zarina.application.extension.CoilApplicationExtension
import ru.livetyping.zarina.application.extension.FavoriteProductIdsFetcherApplicationExtension
import ru.livetyping.zarina.application.extension.MindboxApplicationExtension
import ru.livetyping.zarina.application.extension.TimberApplicationExtension
import ru.livetyping.zarina.application.extension.UserCityFetcherApplicationExtension
import ru.livetyping.zarina.application.extension.UserFetcherApplicationExtension
import ru.livetyping.zarina.application.extension.base.ApplicationExtension

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationExtensionModule {

    @Binds
    @IntoSet
    abstract fun bindBearerTokenFetcherApplicationExtension(
        impl: BearerTokenFetcherApplicationExtension,
    ): ApplicationExtension

    @Binds
    @IntoSet
    abstract fun bindCartProductIdsFetcherApplicationExtension(
        impl: CartProductIdsFetcherApplicationExtension,
    ): ApplicationExtension

    @Binds
    @IntoSet
    abstract fun bindCoilApplicationExtension(impl: CoilApplicationExtension): ApplicationExtension

    @Binds
    @IntoSet
    abstract fun bindFavoriteProductIdsFetcherApplicationExtension(
        impl: FavoriteProductIdsFetcherApplicationExtension,
    ): ApplicationExtension

    @Binds
    @IntoSet
    abstract fun bindMindboxApplicationExtension(
        impl: MindboxApplicationExtension,
    ): ApplicationExtension

    @Binds
    @IntoSet
    abstract fun bindTimberApplicationExtension(
        impl: TimberApplicationExtension,
    ): ApplicationExtension

    @Binds
    @IntoSet
    abstract fun bindUserCityFetcherApplicationExtension(
        impl: UserCityFetcherApplicationExtension,
    ): ApplicationExtension

    @Binds
    @IntoSet
    abstract fun bindUserFetcherApplicationExtension(
        impl: UserFetcherApplicationExtension,
    ): ApplicationExtension
}
