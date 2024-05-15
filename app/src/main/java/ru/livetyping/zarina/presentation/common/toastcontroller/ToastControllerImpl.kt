package ru.livetyping.zarina.presentation.common.toastcontroller

import android.content.Context
import android.widget.Toast
import ru.livetyping.zarina.presentation.base.text.Text
import java.lang.ref.WeakReference

class ToastControllerImpl(private val context: Context) : ToastController {
    private var currentToast: WeakReference<Toast>? = null

    override fun show(text: CharSequence, duration: Int) {
        val toast = Toast.makeText(context, text, duration)
        currentToast = WeakReference(toast)
        toast.show()
    }

    override fun show(text: Text, duration: Int) {
        val string = text.getString(context)
        val toast = Toast.makeText(context, string, duration)
        currentToast = WeakReference(toast)
        toast.show()
    }

    override fun hideCurrentToast() {
        currentToast?.get()?.cancel()
    }
}
