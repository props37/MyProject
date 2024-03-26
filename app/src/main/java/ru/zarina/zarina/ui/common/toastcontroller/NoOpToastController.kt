package ru.zarina.zarina.ui.common.toastcontroller

import ru.zarina.zarina.ui.base.text.Text

class NoOpToastController : ToastController {
    override fun show(text: CharSequence, duration: Int) {
        throw NotImplementedError()
    }

    override fun show(text: Text, duration: Int) {
        throw NotImplementedError()
    }

    override fun hideCurrentToast() {
        throw NotImplementedError()
    }
}
