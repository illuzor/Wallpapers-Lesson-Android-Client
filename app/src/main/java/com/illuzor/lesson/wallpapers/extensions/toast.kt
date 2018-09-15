@file:Suppress("NOTHING_TO_INLINE")

package com.illuzor.lesson.wallpapers.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

inline fun Context.toast(@StringRes id: Int, showLong: Boolean = false): Toast =
        Toast.makeText(this, id, if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).apply { show() }

inline fun Fragment.toast(@StringRes resID: Int, showLong: Boolean = false): Toast = activity!!.toast(resID, showLong)
