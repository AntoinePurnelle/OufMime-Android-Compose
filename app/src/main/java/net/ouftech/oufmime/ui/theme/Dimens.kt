package net.ouftech.oufmime.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.*

@SuppressWarnings("LongParameterList")
class Dimens(
    val bodyText: TextUnit = 16.sp,
    val titleText: TextUnit = 42.sp,
    val bigTitleText: TextUnit = 64.sp,
    val subtitleText: TextUnit = 20.sp,

    val smallButtonText: TextUnit = 16.sp,
    val mediumButtonText: TextUnit = 24.sp,
    val bigButtonText: TextUnit = 32.sp,

    val simpleScoreBoardWidth: Dp = 120.dp,
    val fullScoreBoardWidth: Dp = 200.dp,

    val paddingXSmall: Dp = 2.dp,
    val paddingSmall: Dp = 4.dp,
    val paddingMedium: Dp = 8.dp,
    val paddingLarge: Dp = 16.dp,
    val paddingXLarge: Dp = 32.dp,

    val borderSmall: Dp = 1.dp,
    val borderMedium: Dp = 2.dp,

    val iconSmall: Dp = 100.dp,
    val iconMedium: Dp = iconSmall,

    val timerSize: Dp = 100.dp,
    val timerStrokeWidth: Dp = 6.dp,
) {
    val buttonDimens = mapOf(
        SMALL to smallButtonText,
        MEDIUM to mediumButtonText,
        BIG to bigButtonText
    )
}

val MediumDimens = Dimens()

val ExpandedDimens = Dimens(
    bodyText = 24.sp,
    titleText = 56.sp,
    bigTitleText = 96.sp,
    subtitleText = 32.sp,

    smallButtonText = 24.sp,
    mediumButtonText = 32.sp,
    bigButtonText = 40.sp,

    simpleScoreBoardWidth = 200.dp,
    fullScoreBoardWidth = 400.dp,

    paddingXSmall = 4.dp,
    paddingSmall = 8.dp,
    paddingMedium = 16.dp,
    paddingLarge = 32.dp,
    paddingXLarge = 64.dp,

    borderSmall = 2.dp,
    borderMedium = 4.dp,

    iconSmall = 160.dp,
    iconMedium = 200.dp,

    timerSize = 160.dp,
    timerStrokeWidth = 12.dp
)

enum class ButtonsTextSize { SMALL, MEDIUM, BIG }
