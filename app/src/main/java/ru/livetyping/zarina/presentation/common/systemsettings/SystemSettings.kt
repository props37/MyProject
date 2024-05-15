package ru.livetyping.zarina.presentation.common.systemsettings

import ru.livetyping.zarina.presentation.common.packagename.PackageName

sealed class SystemSettings {
    data class ApplicationDetails(val packageName: PackageName) : SystemSettings()
}
