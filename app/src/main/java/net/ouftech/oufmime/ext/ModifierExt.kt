package net.ouftech.oufmime.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.shadowedBackground(
    shape: Shape,
    elevation: Dp = 6.dp,
    backgroundColor: Color = White,
): Modifier = this
    .shadow(elevation, shape)
    .background(backgroundColor, shape)
    .clip(shape)

fun Modifier.circleShadowedBackground(
    elevation: Dp = 6.dp,
    backgroundColor: Color = White,
) = this.shadowedBackground(CircleShape, elevation, backgroundColor)

fun Modifier.roundedRectShadowedBackground(
    elevation: Dp = 6.dp,
    backgroundColor: Color = White,
) = this.shadowedBackground(RoundedCornerShape(20), elevation, backgroundColor)