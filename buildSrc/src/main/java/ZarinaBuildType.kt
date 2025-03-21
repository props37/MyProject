sealed class ZarinaBuildType(val name: String) {
    open val isDebuggable = false
    open val isMinifyEnabled = true
    open val isShrinkResources = true

    open val isLoggingEnabled: Boolean
        get() = isDebuggable

    open val applicationIdSuffix: String? = null
    open val versionNameSuffix: String? = null

    open val signingVariant = ZarinaSigningVariant.RELEASE

    open val applicationName = APPLICATION_NAME

    open val backendUrl = "https://zarina.ru"
    open val mindboxEndpoint = "ZarinaAppAndroid"
    open val mindboxKey = "GvAwDWq8TZ8eRh340LsM"
    open val recaptchaKey = "6LeII9QmAAAAAFdDn-mO2tUjOZwUYZmM5aqT5GY6"
    open val anyQueryKey = "L1WU1VJBYK"
    open val googleMapsKey = "AIzaSyDxodqz9YA48xbw5T7Nc4iQr9n0YDeBxx0"
    open val appMetricaKey = "0fa7ab19-224f-4c45-9204-135cc022fb4b"

    object Debug : ZarinaBuildType("debug") {
        override val isDebuggable = true
        override val isMinifyEnabled = false
        override val isShrinkResources = false
        override val applicationIdSuffix = ".$name"
        override val versionNameSuffix = "-$name"
        override val signingVariant = ZarinaSigningVariant.INTERNAL
        override val applicationName = "$name $APPLICATION_NAME"
        override val backendUrl = "https://test8.zarina.ru"
        override val mindboxEndpoint = "zarina-android-sandbox"
        override val mindboxKey = "ofzs2DsV9J5PYHAUOrxO"
        override val appMetricaKey = "d43a0f33-15cd-4eed-9a50-e7b107125fbc"
    }

    object Qa : ZarinaBuildType("qa") {
        override val isLoggingEnabled = true
        override val applicationIdSuffix = ".$name"
        override val versionNameSuffix = "-$name"
        override val signingVariant = ZarinaSigningVariant.INTERNAL
        override val applicationName = "$name $APPLICATION_NAME"
        override val backendUrl = "https://test8.zarina.ru"
        override val mindboxEndpoint = "zarina-android-sandbox"
        override val mindboxKey = "ofzs2DsV9J5PYHAUOrxO"
        override val appMetricaKey = "d43a0f33-15cd-4eed-9a50-e7b107125fbc"
    }

    object Release : ZarinaBuildType("release")

    companion object {
        val all: List<ZarinaBuildType>
            get() = listOf(Debug, Qa, Release)

        private const val APPLICATION_NAME = "Zarina"
    }
}
