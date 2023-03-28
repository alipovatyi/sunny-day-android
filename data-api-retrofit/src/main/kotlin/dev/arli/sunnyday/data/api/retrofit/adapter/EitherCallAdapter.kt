@file:Suppress("Filename", "MatchingDeclarationName", "ReturnCount")

package dev.arli.sunnyday.data.api.retrofit.adapter

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dev.arli.sunnyday.data.api.dto.error.ErrorDto
import dev.arli.sunnyday.model.error.ApiError
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class EitherCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != Either::class.java) return null
        check(responseType is ParameterizedType) { "Response type must be a parameterized type." }

        val leftType = getParameterUpperBound(0, responseType)
        if (getRawType(leftType) != ApiError::class.java) return null

        val rightType = getParameterUpperBound(1, responseType)

        val errorDtoConverter = retrofit.nextResponseBodyConverter<Any>(
            /* skipPast = */ null,
            /* type = */ ErrorDto::class.java,
            /* annotations = */ annotations
        )

        return EitherCallAdapter<Any>(rightType, errorDtoConverter)
    }
}

private class EitherCallAdapter<R>(
    private val successType: Type,
    private val errorConverter: Converter<ResponseBody, Any>
) : CallAdapter<R, Call<Either<ApiError, R>>> {

    override fun adapt(call: Call<R>): Call<Either<ApiError, R>> = EitherCall(call, successType, errorConverter)

    override fun responseType(): Type = successType
}

private class EitherCall<R>(
    private val delegate: Call<R>,
    private val successType: Type,
    private val errorConverter: Converter<ResponseBody, Any>
) : Call<Either<ApiError, R>> {

    override fun enqueue(callback: Callback<Either<ApiError, R>>) = delegate.enqueue(
        object : Callback<R> {

            override fun onResponse(call: Call<R>, response: Response<R>) {
                callback.onResponse(this@EitherCall, Response.success(response.toEither()))
            }

            private fun Response<R>.toEither(): Either<ApiError, R> {
                if (isSuccessful.not()) {
                    return when (val errorBody = errorBody()) {
                        null -> ApiError.HttpError(code(), null).left()
                        else -> {
                            val errorDto = runCatching { errorConverter.convert(errorBody) as ErrorDto }.getOrNull()
                            ApiError.HttpError(code(), errorDto?.reason).left()
                        }
                    }
                }

                body()?.let { body -> return body.right() }

                return if (successType == Unit::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    Unit.right() as Either<ApiError, R>
                } else {
                    @Suppress("UNCHECKED_CAST")
                    UnknownError("Response body was null").left() as Either<ApiError, R>
                }
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                val error = when (throwable) {
                    is IOException -> ApiError.NetworkError(throwable)
                    else -> ApiError.UnknownApiError(throwable)
                }
                callback.onResponse(this@EitherCall, Response.success(error.left()))
            }
        }
    )

    override fun clone(): Call<Either<ApiError, R>> = EitherCall(
        delegate = delegate.clone(),
        successType = successType,
        errorConverter = errorConverter
    )

    override fun execute(): Response<Either<ApiError, R>> {
        throw UnsupportedOperationException("Network Response call does not support synchronous execution")
    }

    override fun isExecuted(): Boolean = synchronized(this) { delegate.isExecuted }

    override fun cancel() = synchronized(this) { delegate.cancel() }

    override fun isCanceled(): Boolean = synchronized(this) { delegate.isCanceled }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
