package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.navigation.ScreenResult
import java.util.UUID

@Parcelize
internal data class GiftCertificateScreenResult(
    val isGiftCertificateApplied: Boolean,
    override val id: String = UUID.randomUUID().toString(),
) : ScreenResult, Parcelable {
    companion object {
        const val KEY = "gift_certificate_result"
    }
}
