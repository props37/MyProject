package ru.zarina.zarina.application.extension

import android.app.Application
import cloud.mindbox.mobile_sdk.Mindbox
import cloud.mindbox.mobile_sdk.MindboxConfiguration
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.application.extension.base.ApplicationExtension
import javax.inject.Inject

class MindboxApplicationExtension @Inject constructor() : ApplicationExtension {
    override fun install(application: Application) {
        val configuration = getConfiguration(application)
        Mindbox.init(application, configuration, emptyList())
    }

    private fun getConfiguration(application: Application): MindboxConfiguration {
        return MindboxConfiguration.Builder(
            context = application,
            domain = DOMAIN,
            endpointId = BuildConfig.MINDBOX_ENDPOINT,
        )
            .shouldCreateCustomer(false)
            .subscribeCustomerIfCreated(true)
            .build()
    }

    companion object {
        const val DOMAIN = "api.mindbox.ru"
    }
}
