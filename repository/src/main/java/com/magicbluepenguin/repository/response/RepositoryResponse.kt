package com.magicbluepenguin.repository.response

sealed class ErrorCause
object Network : ErrorCause()
object ServerError : ErrorCause()

data class RepositoryResponse<T>(val data: T, val error: ErrorCause? = null)
