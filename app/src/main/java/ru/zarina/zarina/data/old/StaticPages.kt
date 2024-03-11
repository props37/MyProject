package ru.zarina.zarina.data.old

import ru.zarina.zarina.BuildConfig

object StaticPages {

    const val PRIVACY_POLICY_URL = "${BuildConfig.BACKEND_URL}/help/privacy-policy/"
    const val CONDITIONS_URL = "${BuildConfig.BACKEND_URL}/help/conditions/"
    const val DATA_POLICY_URL = PRIVACY_POLICY_URL

}
