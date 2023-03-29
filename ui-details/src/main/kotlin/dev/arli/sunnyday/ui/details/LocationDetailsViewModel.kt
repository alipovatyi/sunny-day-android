package dev.arli.sunnyday.ui.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arli.sunnyday.ui.common.base.BaseViewModel
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEffect
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEvent
import dev.arli.sunnyday.ui.details.contract.LocationDetailsViewState
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel<LocationDetailsEvent, LocationDetailsViewState, LocationDetailsEffect>() {

    private val args: LocationDetailsScreenArgs by screenArgs(savedStateHandle)

    override fun initialViewState(): LocationDetailsViewState = LocationDetailsViewState

    override fun handleEvent(event: LocationDetailsEvent) {
        // TODO
    }
}
