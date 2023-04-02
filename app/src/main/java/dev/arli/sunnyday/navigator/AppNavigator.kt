package dev.arli.sunnyday.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import dev.arli.sunnyday.ui.navigation.navigator.Navigator
import java.net.URL
import javax.inject.Inject

internal class AppNavigator @Inject internal constructor() : Navigator {

    override fun openUrl(context: Context, url: URL) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
        context.startActivity(intent)
    }
}
