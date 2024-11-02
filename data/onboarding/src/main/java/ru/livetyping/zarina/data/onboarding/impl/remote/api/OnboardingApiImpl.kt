package ru.livetyping.zarina.data.onboarding.impl.remote.api

import ru.livetyping.zarina.core.buildutil.ZarinaBaseUrl
import ru.livetyping.zarina.core.domain.model.common.Url
import javax.inject.Inject

internal class OnboardingApiImpl @Inject constructor(
    @ZarinaBaseUrl
    private val baseUrl: String,
) : OnboardingApi {
    override fun getOnboardingBannerUrl(): Url {
        return Url.create("$baseUrl/api/v1/main/splash/")
    }
}
