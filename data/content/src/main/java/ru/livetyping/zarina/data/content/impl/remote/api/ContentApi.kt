package ru.livetyping.zarina.data.content.impl.remote.api

import ru.livetyping.zarina.core.domain.model.common.Url

internal interface ContentApi {
    fun getOnboardingBannerUrl(): Url
}
