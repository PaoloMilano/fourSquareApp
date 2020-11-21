package com.magicbluepenguin.repository.repositories

sealed class RepositoryResponse<T>

data class SuccessResponse<T>(val data: T?) : RepositoryResponse<T>()
data class ErrorResponse<T>(val data: T?) : RepositoryResponse<T>()
