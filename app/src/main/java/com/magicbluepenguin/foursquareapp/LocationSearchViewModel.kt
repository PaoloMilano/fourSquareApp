package com.magicbluepenguin.foursquareapp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

internal class LocationSearchViewModel @ViewModelInject constructor() : ViewModel() {

    var searchQuery: String? = null
}