package dev.arli.sunnyday.ui.details.contract

import androidx.compose.runtime.Immutable
import dev.arli.sunnyday.model.LocationWithForecasts
import dev.arli.sunnyday.ui.common.base.ViewState

@Immutable
data class LocationDetailsViewState(
    val locationWithForecasts: LocationWithForecasts? = null
) : ViewState
