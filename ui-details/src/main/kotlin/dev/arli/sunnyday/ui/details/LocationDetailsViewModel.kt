package dev.arli.sunnyday.ui.details

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arli.sunnyday.ui.common.base.BaseViewModel
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEffect
import dev.arli.sunnyday.ui.details.contract.LocationDetailsEvent
import dev.arli.sunnyday.ui.details.contract.LocationDetailsViewState
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(

) : BaseViewModel<LocationDetailsEvent, LocationDetailsViewState, LocationDetailsEffect>() {

    override fun initialViewState(): LocationDetailsViewState = LocationDetailsViewState

    override fun handleEvent(event: LocationDetailsEvent) {
        // TODO
    }
}
