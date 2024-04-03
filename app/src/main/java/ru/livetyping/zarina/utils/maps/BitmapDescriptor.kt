package ru.livetyping.zarina.utils.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun Context.getBitmapDescriptor(
    @DrawableRes
    id: Int,
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(this, id) ?: return null

    val width = drawable.intrinsicWidth
    val height = drawable.intrinsicHeight

    drawable.setBounds(0, 0, width, height)

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
