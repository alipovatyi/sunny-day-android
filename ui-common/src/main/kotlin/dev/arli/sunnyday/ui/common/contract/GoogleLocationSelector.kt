package dev.arli.sunnyday.ui.common.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import dev.arli.sunnyday.model.location.Coordinates
import dev.arli.sunnyday.model.location.Latitude
import dev.arli.sunnyday.model.location.Longitude
import dev.arli.sunnyday.model.location.NamedLocation

object GoogleLocationSelector :
    ActivityResultContract<Unit, Either<Throwable, NamedLocation?>>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        val mode = AutocompleteActivityMode.FULLSCREEN
        val fields = listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS)
        val typeFilter = PlaceTypes.CITIES
        return Autocomplete.IntentBuilder(mode, fields)
            .setTypesFilter(listOf(typeFilter))
            .build(context)
    }

    @Suppress("ReturnCount")
    override fun parseResult(resultCode: Int, intent: Intent?): Either<Throwable, NamedLocation?> {
        if (intent == null) {
            return NullPointerException("Intent is null").left()
        }
        return when (resultCode) {
            Activity.RESULT_OK -> {
                val place = Autocomplete.getPlaceFromIntent(intent)
                val latitude = place.latLng?.latitude ?: return NullPointerException("Latitude is null").left()
                val longitude = place.latLng?.longitude ?: return NullPointerException("Longitude is null").left()
                val addressComponents = requireNotNull(place.addressComponents?.asList())
                val locality = addressComponents.first { PlaceTypes.LOCALITY in it.types }
                NamedLocation(
                    coordinates = Coordinates(
                        latitude = Latitude(latitude),
                        longitude = Longitude(longitude)
                    ),
                    name = locality?.name,
                    isCurrent = false
                ).right()
            }
            AutocompleteActivity.RESULT_ERROR -> {
                val status = Autocomplete.getStatusFromIntent(intent)
                val statusMessage = status.statusMessage ?: "Unknown error"
                Throwable(statusMessage).left()
            }
            AutocompleteActivity.RESULT_CANCELED -> null.right()
            else -> Throwable("Unknown error").left()
        }
    }
}
