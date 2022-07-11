package net.common.commonlib.extension

import android.content.Context
import android.util.TypedValue

object FloatExtension {

    /**
     * dp to px
     */
    fun Float.dpToPx(context: Context) : Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)


}