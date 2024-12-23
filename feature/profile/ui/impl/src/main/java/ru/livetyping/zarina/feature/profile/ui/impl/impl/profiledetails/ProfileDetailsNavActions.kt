package ru.livetyping.zarina.feature.profile.ui.impl.impl.profiledetails

internal class ProfileDetailsNavActions(
    val onBackClicked: () -> Unit,
    val onChangePhoneClicked: () -> Unit,
    val onChangeEmailClicked: () -> Unit,
    val onChangePasswordClicked: () -> Unit,
    val onUserSignedOut: () -> Unit,
    val onAccountDeleted: () -> Unit,
)
