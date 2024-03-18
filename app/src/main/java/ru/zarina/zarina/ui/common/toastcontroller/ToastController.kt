package ru.zarina.zarina.ui.common.toastcontroller

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import ru.zarina.zarina.ui.common.text.Text

interface ToastController {
    fun show(text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
    fun show(text: Text, duration: Int = Toast.LENGTH_SHORT)
    fun hideCurrentToast()
}

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
