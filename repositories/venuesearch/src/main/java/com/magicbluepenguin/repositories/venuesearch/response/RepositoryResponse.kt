package com.magicbluepenguin.repositories.venuesearch.response

sealed class ErrorCause
object NetworkError : ErrorCause()
object ServerError : ErrorCause()

data class RepositoryResponse<T>(val data: T, val error: ErrorCause? = null)
