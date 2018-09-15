package com.illuzor.lesson.wallpapers.extensions

import android.os.Handler
import android.os.Looper

fun runInBackground(handler: () -> Unit) = Thread(Runnable(handler)).start()

fun runInMainThread(handler: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        handler()
    } else {
        Handler(Looper.getMainLooper()).post(Runnable(handler))
    }
}
