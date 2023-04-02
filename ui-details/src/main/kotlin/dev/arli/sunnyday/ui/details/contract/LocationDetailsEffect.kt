package dev.arli.sunnyday.ui.details.contract

import dev.arli.sunnyday.ui.common.base.ViewEffect
import java.net.URL

sealed class LocationDetailsEffect : ViewEffect {
    object NavigateUp : LocationDetailsEffect()
    data class OpenUrl(val url: URL) : LocationDetailsEffect()
}
