package ru.livetyping.zarina.presentation.screen.checkout.common

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.domain.checkout.DeliveryOptions
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.util.library.coroutines.FlowRequester

@Stable
sealed class DeliveryOptionsState {
    data object Loading : DeliveryOptionsState()

    @Immutable
    data class Success(val options: List<DeliveryOptionState>) : DeliveryOptionsState()

    @Immutable
    data class Error(val state: ErrorState) : DeliveryOptionsState()

    companion object {
        fun create(
            optionsResult: Result<DeliveryOptions>,
            loadingState: FlowRequester.LoadingState,
            selectedOptionId: DeliveryOptions.Option.Id?,
            deliveryOptionToSelectedDateTimePeriod: Map<DeliveryOptions.Option.Id, DeliveryOptions.Option.DateTimePeriod>,
            getOptionDefaultDateTimePeriod: (option: DeliveryOptions.Option) -> DeliveryOptions.Option.DateTimePeriod,
        ): DeliveryOptionsState {
            return when {
                loadingState.isLoading() -> Loading
                else -> {
                    optionsResult.fold(
                        onSuccess = { options ->
                            val mappedOptions = options.options.mapIndexed { index, option ->
                                val isSelected =
                                    selectedOptionId?.let { option.id == it } ?: (index == 0)
                                val selectedDateTimePeriod =
                                    deliveryOptionToSelectedDateTimePeriod[option.id]
                                        ?: getOptionDefaultDateTimePeriod(option)
                                DeliveryOptionState(
                                    deliveryOption = option,
                                    isSelected = isSelected,
                                    selectedDateTimePeriod = selectedDateTimePeriod,
                                )
                            }
                            Success(mappedOptions)
                        },
                        onFailure = {
                            val errorState = ErrorState.from(it)
                            Error(errorState)
                        },
                    )
                }
            }
        }
    }
}
