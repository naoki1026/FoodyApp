package com.example.foodyapp.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, obsever: Observer<T>) {
    observe(lifecycleOwner, object: Observer<T> {
        override fun onChanged(t: T?) {
            removeObserver(this)
            obsever.onChanged(t)
        }
    })
}