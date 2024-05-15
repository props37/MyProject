package ru.livetyping.zarina.presentation.common.systemsettings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import ru.livetyping.zarina.presentation.common.packagename.PackageName

fun Context.openSettings(
    settings: SystemSettings,
    flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK,
) {
    val intent = getSettingsIntent(settings = settings, context = this, flags = flags)
    this.startActivity(intent)
}

private fun getSettingsIntent(settings: SystemSettings, context: Context, flags: Int): Intent {
    return when (settings) {
        is SystemSettings.ApplicationDetails -> {
            getApplicationDetailsIntent(settings, context, flags)
        }
    }
}

private fun getApplicationDetailsIntent(
    settings: SystemSettings.ApplicationDetails,
    context: Context,
    flags: Int,
): Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
    data = getPackageNameUri(settings.packageName, context)
    this.flags = flags
}

private fun getPackageNameUri(packageName: PackageName, context: Context): Uri {
    return Uri.fromParts("package", packageName.getString(context), null)
}
