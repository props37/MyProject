package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcertificate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.navigation.ScreenResult
import java.util.UUID

@Parcelize
internal data class GiftCertificateScreenResult(
    val isGiftCertificateApplied: Boolean,
    override val id: String = UUID.randomUUID().toString(),
) : ScreenResult, Parcelable
