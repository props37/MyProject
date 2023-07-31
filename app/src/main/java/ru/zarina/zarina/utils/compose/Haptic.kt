package ru.zarina.zarina.utils.compose

import android.content.Context
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.accessibility.AccessibilityManager

enum class HapticType { ERROR }

fun View.performHaptic(type: HapticType) {
    if (context.isTouchExplorationEnabled()) return
    isHapticFeedbackEnabled = true
    val constant = getTypeConstant(type)
    performHapticFeedback(constant)
}

private fun getTypeConstant(type: HapticType): Int {
    return when {
        type == HapticType.ERROR && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> HapticFeedbackConstants.REJECT
        else -> HapticFeedbackConstants.VIRTUAL_KEY
    }
}

private fun Context.isTouchExplorationEnabled(): Boolean {
    val accessibilityManager =
        getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager?
    return accessibilityManager?.isTouchExplorationEnabled ?: false
}