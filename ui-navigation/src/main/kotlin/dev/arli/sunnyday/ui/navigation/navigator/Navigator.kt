package dev.arli.sunnyday.ui.navigation.navigator

import android.content.Context
import java.net.URL

interface Navigator {
    fun openUrl(context: Context, url: URL)
}
