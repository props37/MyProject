sealed class ZarinaBuildType(val name: String) {
    open val isDebuggable = false
    open val isMinifyEnabled = true
    open val isShrinkResources = true

    open val applicationIdSuffix: String? = ".$name"
    open val versionNameSuffix: String? = "-$name"

    open val signingVariant = ZarinaSigningVariant.RELEASE

    open val applicationName = "$name $APPLICATION_NAME"

    open val isLoggingEnabled: Boolean
        get() = isDebuggable

    open val backendUrl = "https://zarina.ru"
    open val mindboxEndpoint = "zarina-android-sandbox"
    open val mindboxKey = "ofzs2DsV9J5PYHAUOrxO"
    open val recaptchaKey = "6LeII9QmAAAAAFdDn-mO2tUjOZwUYZmM5aqT5GY6"
    open val anyQueryKey = "L1WU1VJBYK"
    open val googleMapsKey = "AIzaSyDxodqz9YA48xbw5T7Nc4iQr9n0YDeBxx0"
    open val appMetricaKey = "0fa7ab19-224f-4c45-9204-135cc022fb4b"

    object Debug : ZarinaBuildType("debug") {
        override val isDebuggable = true
        override val isMinifyEnabled = false
        override val isShrinkResources = false
        override val signingVariant = ZarinaSigningVariant.INTERNAL
        override val backendUrl = "https://test7.zarina.ru"
    }

    object Qa : ZarinaBuildType("qa") {
        override val signingVariant = ZarinaSigningVariant.INTERNAL
        override val isLoggingEnabled = true
        override val backendUrl = "https://test7.zarina.ru"
    }

    object Release : ZarinaBuildType("release") {
        override val applicationIdSuffix = null
        override val versionNameSuffix = null
        override val applicationName = APPLICATION_NAME
        override val mindboxEndpoint = "ZarinaAppAndroid"
        override val mindboxKey = "GvAwDWq8TZ8eRh340LsM"
    }

    companion object {
        val all: List<ZarinaBuildType>
            get() = listOf(Debug, Qa, Release)

        private const val APPLICATION_NAME = "Zarina"
    }
}
