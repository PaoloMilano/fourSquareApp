package com.magicbluepenguin.utils.extensions

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

fun Fragment.setSupportActionBar(toolbar: Toolbar): ActionBar = (activity as AppCompatActivity).let {
    it.setSupportActionBar(toolbar)
    requireNotNull(it.supportActionBar)
}