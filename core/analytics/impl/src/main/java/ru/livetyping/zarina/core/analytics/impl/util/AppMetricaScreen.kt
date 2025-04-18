package ru.livetyping.zarina.core.analytics.impl.util

import io.appmetrica.analytics.ecommerce.ECommerceScreen
import ru.livetyping.zarina.core.analytics.model.Screen

internal fun Screen.toECommerceScreen(): ECommerceScreen {
    return ECommerceScreen().apply {
        this.name = this@toECommerceScreen.name
    }
}
