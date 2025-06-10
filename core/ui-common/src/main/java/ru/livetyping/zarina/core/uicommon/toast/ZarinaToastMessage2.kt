package ru.livetyping.zarina.core.uicommon.toast

import androidx.annotation.DrawableRes
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.message.MessageQueue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import ru.livetyping.zarina.core.resource.R as RCommon

// TODO: [Top] Rename after full migration
public data class ZarinaToastMessage2(
    val text: Text,
    val startContent: StartContent? = null,
    val endContent: EndContent? = null,
    val size: Size = Size.Medium,
    val addBracketsToText: Boolean = false,
    override val duration: Duration = DURATION_SHORT,
    override val isRemovable: Boolean = true,
) : MessageQueue.Message {
    public sealed class StartContent {
        public data class Icon(
            @DrawableRes val resId: Int,
            val contentDescription: String?,
        ) : StartContent() {
            public companion object {
                public fun genericError(): Icon {
                    return Icon(
                        resId = RCommon.drawable.ic_exclamation_mark_inscribed_in_triange_24,
                        contentDescription = null,
                    )
                }
            }
        }

        public data class Image(val url: String) : StartContent()
    }

    public sealed class EndContent {
        public data object CloseButton : EndContent()
    }

    public enum class Size { Medium, Large }

    public companion object {
        public val DURATION_SHORT: Duration get() = 3.seconds
        public val DURATION_LONG: Duration get() = 5.seconds

        public fun genericError(): ZarinaToastMessage2 {
            return ZarinaToastMessage2(
                text = Text.Resource(RCommon.string.res_something_went_wrong_try_again),
                startContent = StartContent.Icon.genericError(),
            )
        }

        public fun networkError(): ZarinaToastMessage2 {
            return ZarinaToastMessage2(
                text = Text.Resource(RCommon.string.res_check_internet_connection),
                startContent = StartContent.Icon(
                    resId = RCommon.drawable.ic_wifi_error_24,
                    contentDescription = null,
                ),
            )
        }

        public fun productAddedToWishlist(product: Product): ZarinaToastMessage2 {
            val imageUrl = product.media.firstOrNull()?.thumbnailUrl?.value.orEmpty()
            return ZarinaToastMessage2(
                text = Text.Resource(RCommon.string.res_product_added_to_wishlist),
                startContent = StartContent.Image(imageUrl),
                endContent = EndContent.CloseButton,
                size = Size.Large,
                addBracketsToText = true,
            )
        }
    }
}
