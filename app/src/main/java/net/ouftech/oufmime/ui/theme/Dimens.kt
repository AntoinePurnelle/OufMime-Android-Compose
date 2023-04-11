/*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package net.ouftech.oufmime.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.*

@SuppressWarnings("LongParameterList")
data class Dimens(
    val bodyText: TextUnit = 16.sp,
    val titleText: TextUnit = 42.sp,
    val bigTitleText: TextUnit = 64.sp,
    val subtitleText: TextUnit = 20.sp,
    val wordCardWordText: TextUnit = 50.sp,
    val wordCardCategoryText: TextUnit = 40.sp,
    val cardDeckText: TextUnit = 36.sp,

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

    val iconSmall: Dp = 60.dp,
    val iconMedium: Dp = 100.dp,

    val smallIconButton: Dp = iconSmall,
    val bigIconButton: Dp = 120.dp,

    val playWidgetsSize: Dp = 100.dp,
    val cardHeight: Dp = 80.dp,
    val cardWidth: Dp = 60.dp,
    val cardPadding: Dp = 4.dp,
    val cardCornerRadius: Dp = 10.dp,
    val widgetRotation: Float = 20f,

    val timerStrokeWidth: Dp = 64.dp,

    val isExpandedScreen: Boolean = false,

    val defaultShadow: Dp = 8.dp
) {
    val buttonDimens = mapOf(
        SMALL to smallButtonText,
        MEDIUM to mediumButtonText,
        BIG to bigButtonText
    )
}

val MediumDimens = Dimens()

val ExpandedDimens = Dimens(
    bodyText = 28.sp,
    titleText = 60.sp,
    bigTitleText = 96.sp,
    subtitleText = 36.sp,
    wordCardWordText = 100.sp,
    wordCardCategoryText = 80.sp,
    cardDeckText = 64.sp,

    smallButtonText = 32.sp,
    mediumButtonText = 40.sp,
    bigButtonText = 48.sp,

    simpleScoreBoardWidth = 200.dp,
    fullScoreBoardWidth = 500.dp,

    paddingXSmall = 4.dp,
    paddingSmall = 8.dp,
    paddingMedium = 16.dp,
    paddingLarge = 32.dp,
    paddingXLarge = 64.dp,

    borderSmall = 2.dp,
    borderMedium = 4.dp,

    iconSmall = 100.dp,
    iconMedium = 200.dp,

    smallIconButton = 100.dp,
    bigIconButton = 200.dp,

    playWidgetsSize = 160.dp,
    timerStrokeWidth = 12.dp,
    cardHeight = 128.dp,
    cardWidth = 96.dp,
    cardPadding = 6.dp,
    cardCornerRadius = 16.dp,

    isExpandedScreen = true,
)

enum class ButtonsTextSize { SMALL, MEDIUM, BIG }