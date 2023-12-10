package com.example.course2.ui.modifiers

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp


fun Modifier.shimmer(
    enabled: Boolean,
    cornerRadius: CornerRadius = CornerRadius(100.dp.value)
) = composed {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val endOffset = 1000f

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = endOffset,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing,
//                delayMillis = 500
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )


    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    var height by remember {
        mutableFloatStateOf(0f)
    }
    if (enabled) {
        onGloballyPositioned {
            height = it.size.height.toFloat()
        }.drawWithContent {
            clipRect {
                drawRoundRect(
                    brush,
                    blendMode = BlendMode.Luminosity,
                    cornerRadius = cornerRadius,
                    size = Size(
                        this@drawWithContent.size.width,
                        height
                    )
                )
            }
        }
    } else {
        this
    }
}
