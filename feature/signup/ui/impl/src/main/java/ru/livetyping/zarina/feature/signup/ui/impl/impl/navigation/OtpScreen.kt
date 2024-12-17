package ru.livetyping.zarina.feature.signup.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signup.ui.impl.impl.otp.OtpNavActions
import ru.livetyping.zarina.feature.signup.ui.impl.impl.otp.OtpNavEntry
import ru.livetyping.zarina.feature.signup.ui.impl.impl.otp.OtpScreen

internal fun NavGraphBuilder.otpScreen(actions: OtpNavActions) {
    composable<OtpNavEntry> {
        OtpScreen(actions)
    }
}
