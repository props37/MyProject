package ru.livetyping.zarina.application.extension

import android.app.Application
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import io.appmetrica.analytics.profile.Attribute
import io.appmetrica.analytics.profile.GenderAttribute
import io.appmetrica.analytics.profile.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.application.extension.base.ApplicationExtension
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.model.user.User
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import javax.inject.Inject

class AppMetricaApplicationExtension @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val getUserFlowUseCase: GetUserFlowUseCase,
) : ApplicationExtension {

    override fun install(application: Application) {
        initializeAppMetrica(application)
        enableAppMetricaUserProfileUpdate()
    }

    private fun initializeAppMetrica(application: Application) {
        val config = AppMetricaConfig.newConfigBuilder(BuildConfig.APP_METRICA_KEY).apply {
            if (BuildConfig.IS_LOGGING_ENABLED) withLogs()
        }.build()
        AppMetrica.activate(application, config)
    }

    private fun enableAppMetricaUserProfileUpdate() {
        getUserFlowUseCase(GetUserFlowUseCase.Params(CachePolicy.LocalOnly))
            .map { result ->
                result.getOrNull()
            }
            .distinctUntilChanged()
            .onEach { user ->
                AppMetrica.setUserProfileID(user?.id?.value)
                val userProfile = createUserProfile(user)
                AppMetrica.reportUserProfile(userProfile)
            }
            .launchIn(coroutineScope)
    }

    private fun createUserProfile(user: User?): UserProfile {
        return UserProfile.newBuilder().apply {
            val genderAttr = when (user?.gender) {
                Gender.FEMALE -> Attribute.gender().withValue(GenderAttribute.Gender.FEMALE)
                Gender.MALE -> Attribute.gender().withValue(GenderAttribute.Gender.MALE)
                null -> Attribute.gender().withValueReset()
            }
            apply(genderAttr)

            val birthDate = user?.birthDate
            val ageAttr = if (birthDate != null) {
                Attribute.birthDate().withBirthDate(
                    /* year = */ birthDate.year,
                    /* month = */ birthDate.monthValue,
                    /* dayOfMonth = */ birthDate.dayOfMonth,
                )
            } else {
                Attribute.birthDate().withValueReset()
            }
            apply(ageAttr)
        }.build()
    }
}
