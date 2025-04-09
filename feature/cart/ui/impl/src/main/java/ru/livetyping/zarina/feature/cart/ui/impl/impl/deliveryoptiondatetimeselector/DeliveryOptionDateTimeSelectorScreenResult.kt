package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryoptiondatetimeselector

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.navigation.ScreenResult
import ru.livetyping.zarina.core.uimodel.checkout.DeliveryOptionParcelable
import java.util.UUID

@Parcelize
internal data class DeliveryOptionDateTimeSelectorScreenResult(
    val selectorType: DeliveryOptionDateTimeSelectorType,
    private val deliveryOptionId: String,
    val dateTimePeriod: DeliveryOptionParcelable.DateTimePeriodParcelable,
    override val id: String = UUID.randomUUID().toString(),
) : ScreenResult, Parcelable {
    fun getDeliveryOptionId(): DeliveryOption.Id = DeliveryOption.Id(deliveryOptionId)

    companion object {
        const val KEY = "delivery_option_date_time_selector_result_key"
    }
}
