package dev.arli.sunnyday.data.device.datasource

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Build
import arrow.core.Either
import arrow.core.continuations.either
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dev.arli.sunnyday.data.common.di.IoDispatcher
import dev.arli.sunnyday.data.location.datasource.DeviceLocationDataSource
import dev.arli.sunnyday.domain.model.location.Coordinates
import dev.arli.sunnyday.domain.model.location.Latitude
import dev.arli.sunnyday.domain.model.location.Longitude
import dev.arli.sunnyday.domain.model.location.NamedLocation
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

internal class DeviceLocationDataSourceImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DeviceLocationDataSource {

    override suspend fun getCurrentLocation(): Either<Throwable, NamedLocation?> {
        return either {
            val coordinates = getCurrentLocationCoordinates().bind() ?: return@either null
            val address = getLocationAddress(coordinates).bind()
            NamedLocation(coordinates = coordinates, name = address?.locality)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocationCoordinates(): Either<Throwable, Coordinates?> {
        return Either.catch {
            val priority = Priority.PRIORITY_LOW_POWER
            val cancellationToken = object : CancellationToken() {
                override fun onCanceledRequested(listener: OnTokenCanceledListener) = CancellationTokenSource().token
                override fun isCancellationRequested() = false
            }
            withContext(ioDispatcher) {
                fusedLocationProviderClient.getCurrentLocation(priority, cancellationToken).await()
            }?.let { location ->
                Coordinates(
                    latitude = Latitude(location.latitude),
                    longitude = Longitude(location.longitude)
                )
            }
        }
    }

    // TODO: consider replacing Geocoder with Google Geocoding API
    private suspend fun getLocationAddress(coordinates: Coordinates): Either<Throwable, Address?> {
        val (latitude, longitude) = coordinates.latitude.value to coordinates.longitude.value
        return Either.catch {
            withContext(ioDispatcher) {
                suspendCancellableCoroutine { continuation ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                            continuation.resume(addresses.firstOrNull())
                        }
                    } else {
                        continuation.resume(geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull())
                    }
                }
            }
        }
    }
}
