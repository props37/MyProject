package ru.zarina.zarina.base.application.extensions

import android.app.Application
import cloud.mindbox.mobile_sdk.Mindbox
import cloud.mindbox.mobile_sdk.MindboxConfiguration
import org.koin.core.annotation.Factory
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.base.application.extensions.base.ApplicationExtension
import javax.inject.Inject

@Factory
class MindboxExtension @Inject constructor() : ApplicationExtension {

    override fun install(application: Application) {
        val configuration = getConfiguration(application)
        Mindbox.init(application, configuration, emptyList())
    }

    private fun getConfiguration(application: Application) = MindboxConfiguration.Builder(
        context = application.applicationContext,
        domain = DOMAIN,
        endpointId = BuildConfig.MINDBOX_ENDPOINT,
    )
        .shouldCreateCustomer(false)
        .subscribeCustomerIfCreated(true)
        .build()

    companion object {
        const val DOMAIN = "api.mindbox.ru"
    }

}
