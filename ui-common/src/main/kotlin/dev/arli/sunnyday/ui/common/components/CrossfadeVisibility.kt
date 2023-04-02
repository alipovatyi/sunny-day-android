package dev.arli.sunnyday.ui.common.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CrossfadeVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    label: String = "Crossfade",
    content: @Composable () -> Unit
) {
    Crossfade(
        targetState = visible,
        modifier = modifier,
        animationSpec = animationSpec,
        label = label
    ) { isVisible ->
        if (isVisible) {
            content()
        }
    }
}
