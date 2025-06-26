package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

@Stable
internal sealed class DeliveryOptionsState {
    fun findSelectedDeliveryOption(): DeliveryOptionState? {
        return if (this is Success) {
            this.options.find { it.isSelected }
        } else null
    }

    @Immutable
    data class Success(val options: ImmutableList<DeliveryOptionState>) : DeliveryOptionsState()

    data object Loading : DeliveryOptionsState()

    @Immutable
    data class Error(val state: ZarinaErrorScreenState) : DeliveryOptionsState()

    data object None : DeliveryOptionsState()

    class Builder {
        fun build(
            deliveryOptionsResult: Result<List<DeliveryOption>>?,
            isLoading: Boolean,
            selectedDeliveryOptionId: DeliveryOption.Id?,
            deliveryOptionSelectedDateTimePeriodProvider: (option: DeliveryOption) -> DeliveryOption.DateTimePeriod?,
        ): DeliveryOptionsState {
            return when {
                isLoading -> Loading
                deliveryOptionsResult != null -> {
                    deliveryOptionsResult.fold(
                        onSuccess = { options ->
                            val mappedOptions = options
                                .mapIndexed { index, option ->
                                    val isSelected =
                                        selectedDeliveryOptionId?.let { option.id == it } ?: (index == 0)
                                    val selectedDateTimePeriod =
                                        deliveryOptionSelectedDateTimePeriodProvider(option)
                                    if (selectedDateTimePeriod != null) {
                                        DeliveryOptionState(
                                            deliveryOption = option,
                                            isSelected = isSelected,
                                            selectedDateTimePeriod = selectedDateTimePeriod,
                                        )
                                    } else {
                                        val errorState = ZarinaErrorScreenState.NETWORK
                                        return@fold Error(errorState)
                                    }
                                }
                            Success(mappedOptions.toImmutableList())
                        },
                        onFailure = { t ->
                            val errorState = ZarinaErrorScreenState.from(t)
                            Error(errorState)
                        }
                    )
                }

                else -> None
            }
        }
    }
}
