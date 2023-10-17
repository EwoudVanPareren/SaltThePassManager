package nl.vanparerensoftwaredevelopment.saltthepassmanager.common.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * If this [Context] is an [Activity], or if it wraps one (directly
 * or indirectly), then return the [Activity].
 * Otherwise, return null.
 */
internal fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}