package ru.livetyping.zarina.presentation.notification.push

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import ru.livetyping.zarina.application.ZarinaApplication
import timber.log.Timber

object AppStorePushManager {
    fun isAppStorePush(actionUrl: String): Boolean = actionUrl == APP_STORE_ACTION_URL

    fun getAppStorePageUri(context: Context): Uri {
        val installer = getInstaller(context)
        return getAppStorePageUri(installer)
    }

    private fun getInstaller(context: Context): Installer {
        val installerPackageName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getInstallerPackageNameApi30AndAbove(context)
        } else {
            getInstallerPackageNameApi29AndBelow(context)
        }
        return when (installerPackageName) {
            GOOGLE_PLAY_PACKAGE_NAME -> Installer.GOOGLE_PLAY
            else -> Installer.UNKNOWN
        }
    }

    private fun getAppStorePageUri(installer: Installer): Uri {
        return when (installer) {
            Installer.GOOGLE_PLAY -> getGooglePlayPageUri()
            Installer.UNKNOWN -> getGooglePlayPageUri()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getInstallerPackageNameApi30AndAbove(context: Context): String? {
        return try {
            val sourceInfo =
                context.packageManager.getInstallSourceInfo(ZarinaApplication.RELEASE_PACKAGE_NAME)
            sourceInfo.installingPackageName
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
            null
        }
    }

    @Suppress("DEPRECATION")
    private fun getInstallerPackageNameApi29AndBelow(context: Context): String? {
        return try {
            context.packageManager.getInstallerPackageName(ZarinaApplication.RELEASE_PACKAGE_NAME)
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
            null
        }
    }

    private fun getGooglePlayPageUri(): Uri {
        val releasePackageName = ZarinaApplication.RELEASE_PACKAGE_NAME
        return "$GOOGLE_PLAY_PAGE_PREFIX$releasePackageName".toUri()
    }

    private enum class Installer {
        GOOGLE_PLAY,
        UNKNOWN,
    }

    private const val APP_STORE_ACTION_URL = "content://app-store"

    private const val GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending"

    private const val GOOGLE_PLAY_PAGE_PREFIX = "https://play.google.com/store/apps/details?id="

    private const val TAG = "AppStorePushManager"
}
