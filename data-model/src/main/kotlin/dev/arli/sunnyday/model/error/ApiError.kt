package dev.arli.sunnyday.model.error

sealed class ApiError : Throwable() {
    data class HttpError(val code: Int, val reason: String?) : ApiError()
    data class NetworkError(val throwable: Throwable) : ApiError()
    data class UnknownApiError(val throwable: Throwable) : ApiError()
}
