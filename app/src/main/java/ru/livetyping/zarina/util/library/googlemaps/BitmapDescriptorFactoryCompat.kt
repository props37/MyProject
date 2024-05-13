package ru.livetyping.zarina.util.library.googlemaps

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmapOrNull
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object BitmapDescriptorFactoryCompat {
    fun fromVectorResource(
        @DrawableRes resourceId: Int,
        context: Context,
        @Px width: Int = INTRINSIC_WIDTH,
        @Px height: Int = INTRINSIC_HEIGHT,
    ): BitmapDescriptor? {
        val drawable = AppCompatResources.getDrawable(context, resourceId)
        val bitmap = drawable?.toBitmapOrNull(width, height)
        return if (bitmap != null) BitmapDescriptorFactory.fromBitmap(bitmap) else null
    }

    private const val INTRINSIC_WIDTH = -1
    private const val INTRINSIC_HEIGHT = -1
}
