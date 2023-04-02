package dev.arli.sunnyday.ui.details.contract

import dev.arli.sunnyday.ui.common.base.ViewEvent

sealed class LocationDetailsEvent : ViewEvent {
    object BackClick : LocationDetailsEvent()
    object DeleteClick : LocationDetailsEvent()
    object CopyrightClick : LocationDetailsEvent()
    object Refresh : LocationDetailsEvent()
}
