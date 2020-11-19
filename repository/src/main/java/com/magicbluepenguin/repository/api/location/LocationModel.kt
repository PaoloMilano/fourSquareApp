package com.magicbluepenguin.repository.api.location

internal data class LocationSearchApiResponse(val response: LocationResponse)

internal data class LocationResponse(val venues: List<LocationModel>)

data class LocationModel(
    val id: String,
    val name: String,
    val location: Location
)

data class Location(val formattedAddress: List<String>)