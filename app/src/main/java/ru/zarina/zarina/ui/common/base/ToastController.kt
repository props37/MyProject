package ru.zarina.zarina.ui.common.base

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import java.lang.ref.WeakReference

val LocalToastController = staticCompositionLocalOf<ToastController> {
    NoOpToastController()
}

@Composable
fun rememberToastController(): ToastController {
    val context = LocalContext.current
    return remember(context) {
        ToastControllerImpl(context)
    }
}

interface ToastController {
    fun show(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
    fun show(text: Text, duration: Int = Toast.LENGTH_SHORT)
    fun hideCurrentToast()
}

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

private class NoOpToastController : ToastController {
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
