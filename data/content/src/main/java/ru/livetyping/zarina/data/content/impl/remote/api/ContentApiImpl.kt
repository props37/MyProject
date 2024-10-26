package ru.livetyping.zarina.data.content.impl.remote.api

import ru.livetyping.zarina.core.buildutil.ZarinaBaseUrl
import ru.livetyping.zarina.core.domain.model.common.Url
import javax.inject.Inject

internal class ContentApiImpl @Inject constructor(
    @ZarinaBaseUrl
    private val baseUrl: String,
) : ContentApi {
    override fun getOnboardingBannerUrl(): Url {
        return Url.create("$baseUrl/api/v1/main/splash/")
    }
}
