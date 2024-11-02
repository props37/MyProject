package ru.livetyping.zarina.data.onboarding.impl.remote.api

import ru.livetyping.zarina.core.domain.model.common.Url

internal interface OnboardingApi {
    fun getOnboardingBannerUrl(): Url
}
