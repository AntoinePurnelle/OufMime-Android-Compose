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

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.MEDIUM

data class ScreenConfiguration(val widthClass: WindowWidthSizeClass, val heightClass: WindowHeightSizeClass) {
    constructor(windowSize: WindowSizeClass) : this(windowSize.widthSizeClass, windowSize.heightSizeClass) {
        current = this
    }

    val dimens = when {
        widthClass.isCompact() -> CompactPortraitDimens
        heightClass.isCompact() -> CompactLandscapeDimens
        widthClass.isExpanded() -> ExpandedLandscapeDimens
        heightClass.isExpanded() -> ExpandedPortraitDimens
        else -> MediumDimens
    }

    companion object {
        val COMPACT_PORTRAIT = ScreenConfiguration(WindowWidthSizeClass.Compact, WindowHeightSizeClass.Medium)
        val COMPACT_LANDSCAPE = ScreenConfiguration(WindowWidthSizeClass.Medium, WindowHeightSizeClass.Compact)
        val MEDIUM = ScreenConfiguration(WindowWidthSizeClass.Medium, WindowHeightSizeClass.Medium)
        val EXPANDED_PORTRAIT = ScreenConfiguration(WindowWidthSizeClass.Medium, WindowHeightSizeClass.Expanded)
        val EXPANDED_LANDSCAPE = ScreenConfiguration(WindowWidthSizeClass.Expanded, WindowHeightSizeClass.Medium)

        var current = MEDIUM
            private set
    }

}

val LocalScreenConfiguration = compositionLocalOf { MEDIUM }

@Composable
fun isExpandedLandscape() = LocalScreenConfiguration.current.widthClass.isExpanded()

@Composable
fun isCompactLandscape() = LocalScreenConfiguration.current.heightClass.isCompact()
fun WindowWidthSizeClass.isCompact() = this == WindowWidthSizeClass.Compact
fun WindowWidthSizeClass.isMedium() = this == WindowWidthSizeClass.Medium
fun WindowWidthSizeClass.isExpanded() = this == WindowWidthSizeClass.Expanded
fun WindowHeightSizeClass.isCompact() = this == WindowHeightSizeClass.Compact
fun WindowHeightSizeClass.isMedium() = this == WindowHeightSizeClass.Medium
fun WindowHeightSizeClass.isExpanded() = this == WindowHeightSizeClass.Expanded