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

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.BIG
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.MEDIUM
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.SMALL

@SuppressWarnings("LongParameterList")
data class Dimens(
    val screenSize: ScreenSize
) {

    val bodyLarge: TextUnit = bodyLargeSized.get(screenSize)
    val bodyMedium: TextUnit = bodyMediumSized.get(screenSize)
    val bodySmall: TextUnit = bodySmallSized.get(screenSize)
    val titleText: TextUnit = titleTextSized.get(screenSize)
    val bigTitleText: TextUnit = bigTitleTextSized.get(screenSize)
    val subtitleText: TextUnit = subtitleTextSized.get(screenSize)
    val wordCardWordText: TextUnit = wordCardWordTextSized.get(screenSize)
    val wordCardCategoryText: TextUnit = wordCardCategoryTextSized.get(screenSize)
    val cardDeckText: TextUnit = cardDeckTextSized.get(screenSize)

    val smallButtonText: TextUnit = smallButtonTextSized.get(screenSize)
    val mediumButtonText: TextUnit = mediumButtonTextSized.get(screenSize)
    val bigButtonText: TextUnit = bigButtonTextSized.get(screenSize)

    val simpleScoreBoardWidth: Dp = simpleScoreBoardWidthSized.get(screenSize)
    val fullScoreBoardWidth: Dp = fullScoreBoardWidthSized.get(screenSize)

    val paddingSmall: Dp = paddingSmallSized.get(screenSize)
    val paddingMedium: Dp = paddingMediumSized.get(screenSize)
    val paddingLarge: Dp = paddingLargeSized.get(screenSize)
    val paddingXLarge: Dp = paddingXLargeSized.get(screenSize)
    val paddingXXLarge: Dp = paddingXXLargeSized.get(screenSize)

    val iconSmall: Dp = iconSmallSized.get(screenSize)
    val iconMedium: Dp = iconMediumSized.get(screenSize)
    val iconLarge: Dp = iconLargeSized.get(screenSize)

    val smallIconButton: Dp = mediumIconButtonSized.get(screenSize)
    val bigIconButton: Dp = largeIconButtonSized.get(screenSize)

    val playWidgetsSize: Dp = playWidgetsSizeSized.get(screenSize)
    val cardHeight: Dp = cardHeightSized.get(screenSize)
    val cardWidth: Dp = cardWidthSized.get(screenSize)
    val cardPadding: Dp = cardPaddingSized.get(screenSize)
    val cardCornerRadius: Dp = cardCornerRadiusSized.get(screenSize)

    val largeCardMaxWidth: Dp = largeCardMaxWidthSized.get(screenSize)
    val wordsCardMaxWidth: Dp = wordsCardMaxWidthSized.get(screenSize)
    val wordsCardMaxWidthOuterBounds: Dp = wordsCardMaxWidthOuterBoundsSized.get(screenSize)

    val buttonDimens = mapOf(
        SMALL to smallButtonText,
        MEDIUM to mediumButtonText,
        BIG to bigButtonText
    )

    companion object {

        val bodyLargeSized = SizedSp(20.sp, 30.sp, 40.sp)
        val bodyMediumSized = SizedSp(20.sp, 28.sp, 36.sp)
        val bodySmallSized = SizedSp(8.sp, 12.sp, 16.sp)
        val titleTextSized = SizedSp(40.sp, 44.sp, 48.sp)
        val bigTitleTextSized = SizedSp(64.sp, 80.sp, 96.sp)
        val subtitleTextSized = SizedSp(20.sp, 30.sp, 40.sp)
        val wordCardWordTextSized = SizedSp(50.sp, 75.sp, 100.sp)
        val wordCardCategoryTextSized = SizedSp(32.sp, 48.sp, 64.sp)
        val cardDeckTextSized = SizedSp(36.sp, 50.sp, 64.sp)

        val smallButtonTextSized = SizedSp(16.sp, 24.sp, 32.sp)
        val mediumButtonTextSized = SizedSp(24.sp, 32.sp, 40.sp)
        val bigButtonTextSized = SizedSp(32.sp, 40.sp, 48.sp)

        val simpleScoreBoardWidthSized = SizedDp(120.dp, 160.dp, 200.dp)
        val fullScoreBoardWidthSized = SizedDp(200.dp, 300.dp, 350.dp, 500.dp, 500.dp)

        val paddingSmallSized = SizedDp(4.dp, 6.dp, 8.dp)
        val paddingMediumSized = SizedDp(8.dp, 12.dp, 16.dp)
        val paddingLargeSized = SizedDp(16.dp, 24.dp, 32.dp)
        val paddingXLargeSized = SizedDp(32.dp, 48.dp, 64.dp)
        val paddingXXLargeSized = SizedDp(64.dp, 96.dp, 128.dp)

        val iconSmallSized = SizedDp(24.dp, 32.dp, 40.dp)
        val iconMediumSized = SizedDp(60.dp, 80.dp, 100.dp)
        val iconLargeSized = SizedDp(60.dp, 110.dp, 140.dp)

        val mediumIconButtonSized = iconMediumSized
        val largeIconButtonSized = SizedDp(120.dp, 160.dp, 200.dp)

        val playWidgetsSizeSized = SizedDp(100.dp, 130.dp, 160.dp)
        val cardHeightSized = SizedDp(80.dp, 100.dp, 120.dp)
        val cardWidthSized = SizedDp(60.dp, 80.dp, 100.dp)
        val cardPaddingSized = SizedDp(4.dp, 5.dp, 6.dp)
        val cardCornerRadiusSized = SizedDp(10.dp, 14.dp, 16.dp)

        val largeCardMaxWidthSized = SizedDp(600.dp, 700.dp, 800.dp)
        val wordsCardMaxWidthSized = SizedDp(400.dp, 700.dp, 800.dp)
        val wordsCardMaxWidthOuterBoundsSized = SizedDp(500.dp, 800.dp, 900.dp)
    }
}

val CompactPortraitDimens = Dimens(ScreenSize.CP)
val CompactLandscapeDimens = Dimens(ScreenSize.CL)
val MediumDimens = Dimens(ScreenSize.M)
val ExpandedPortraitDimens = Dimens(ScreenSize.EP)
val ExpandedLandscapeDimens = Dimens(ScreenSize.EL)

@Composable
fun getDimens() = LocalScreenConfiguration.current.dimens

data class SizedDp(
    val cp: Dp,
    val cl: Dp,
    val m: Dp,
    val ep: Dp,
    val el: Dp,
) {
    constructor(c: Dp, m: Dp, e: Dp) : this(c, c, m, e, e)

    fun get(size: ScreenSize) = when (size) {
        ScreenSize.CP -> cp
        ScreenSize.CL -> cl
        ScreenSize.M -> m
        ScreenSize.EP -> ep
        ScreenSize.EL -> el
    }
}

data class SizedSp(
    val c: TextUnit,
    val m: TextUnit,
    val e: TextUnit,
) {
    fun get(size: ScreenSize) = when (size) {
        ScreenSize.CP, ScreenSize.CL -> c
        ScreenSize.M -> m
        ScreenSize.EP, ScreenSize.EL -> e
    }
}

enum class ScreenSize { CP, CL, M, EP, EL }

enum class ButtonsTextSize { SMALL, MEDIUM, BIG }