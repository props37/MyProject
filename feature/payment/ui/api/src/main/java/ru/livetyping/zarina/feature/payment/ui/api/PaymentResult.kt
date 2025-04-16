package ru.livetyping.zarina.feature.payment.ui.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.navigation.ScreenResult
import java.util.UUID

@Parcelize
public data class PaymentResult(
    override val id: String = UUID.randomUUID().toString(),
) : ScreenResult, Parcelable {
    public companion object {
        public const val KEY: String = "payment_result_key"
    }
}
