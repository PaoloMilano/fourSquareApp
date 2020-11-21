package com.magicbluepenguin.utils.extensions

import android.view.View

fun View.heightDP() = (height / context.resources.displayMetrics.density).toInt()
fun View.widthDP() = (width / context.resources.displayMetrics.density).toInt()
