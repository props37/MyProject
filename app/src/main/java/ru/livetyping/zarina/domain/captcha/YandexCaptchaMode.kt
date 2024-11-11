package ru.livetyping.zarina.domain.captcha

enum class YandexCaptchaMode {
    SLIDER,
    CHECKBOX;

    fun getNext(): YandexCaptchaMode? {
        return when (this) {
            SLIDER -> CHECKBOX
            CHECKBOX -> null
        }
    }

    companion object {
        fun getMain(): YandexCaptchaMode = SLIDER

        const val MODE_MAX_DURATION_MILLIS = 10_000L
    }
}
