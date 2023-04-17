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

package net.ouftech.oufmime.ui.views.screens.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.ui.theme.ScreenConfiguration
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.MEDIUM
import net.ouftech.oufmime.ui.theme.ScreenConfiguredTheme
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.theme.isCompactLandscape
import net.ouftech.oufmime.ui.theme.isExpandedLandscape
import net.ouftech.oufmime.ui.views.library.CompactLandscapePreview
import net.ouftech.oufmime.ui.views.library.CompactPortraitPreview
import net.ouftech.oufmime.ui.views.library.ExpandedLandscapePreview
import net.ouftech.oufmime.ui.views.library.ExpandedPortraitPreview
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullScreenRow
import net.ouftech.oufmime.ui.views.library.MediumPreview
import net.ouftech.oufmime.ui.views.library.SplitBackgrounded

// region Views

@Composable
fun SettingsScreen(
    uiState: SettingsScreenUiState,
    listener: SettingsScreenListener
) = SplitBackgrounded {
    with(uiState) {
        if (isExpandedLandscape() || isCompactLandscape()) {
            FullScreenRow {
                CategoriesPickerView(
                    selectedCategories = selectedCategories,
                    onCategoryClick = listener::onCategoryClick
                )

                InputFieldsView(
                    wordsCount = wordsCount,
                    timerTotalTime = timerTotalTime,
                    onStartClick = listener::onStartClick,
                    onLanguageClick = listener::onLanguageClick
                )
            }
        } else {
            FullScreenColumn(modifier = Modifier.padding(40.dp)) {
                CategoriesPickerView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    selectedCategories = selectedCategories,
                    onCategoryClick = listener::onCategoryClick
                )

                Spacer(modifier = Modifier.height(getDimens().paddingLarge))

                InputFieldsView(
                    wordsCount = wordsCount,
                    timerTotalTime = timerTotalTime,
                    onStartClick = listener::onStartClick,
                    onLanguageClick = listener::onLanguageClick
                )
            }
        }
    }
}

// endregion Views

// region Previews

@CompactPortraitPreview
@Composable
private fun CompactPortraitPreviewSettingsScreen() = SettingsScreenPreview(COMPACT_PORTRAIT)

@CompactLandscapePreview
@Composable
private fun CompactLandscapePreviewSettingsScreen() = SettingsScreenPreview(COMPACT_LANDSCAPE)

@MediumPreview
@Composable
private fun MediumPreviewSettingsScreen() = SettingsScreenPreview(MEDIUM)

@ExpandedPortraitPreview
@Composable
private fun ExpandedPortraitPreviewSettingsScreen() = SettingsScreenPreview(EXPANDED_PORTRAIT)

@ExpandedLandscapePreview
@Composable
private fun ExpandedLandscapePreviewSettingsScreen() = SettingsScreenPreview(EXPANDED_LANDSCAPE)

@Composable
private fun SettingsScreenPreview(screenConfiguration: ScreenConfiguration) = ScreenConfiguredTheme(screenConfiguration) {
    SettingsScreen(
        uiState = stubSettingsScreenUiState,
        listener = stubSettingsScreenListener,
    )
}

// endregion Previews

data class SettingsScreenUiState(
    val selectedCategories: Map<String, Boolean>,
    val wordsCount: Int,
    val timerTotalTime: Long,
)

private val stubSettingsScreenUiState
    get() = SettingsScreenUiState(
        selectedCategories = Categories.values().map { it.name to true }.toMutableStateMap(),
        wordsCount = 40,
        timerTotalTime = 40,
    )

private val stubSettingsScreenListener
    get() = object : SettingsScreenListener {
        override fun onCategoryClick(category: String, selected: Boolean) = Unit
        override fun onStartClick(wordsCount: Int, timerTotalTime: Long) = Unit
        override fun onLanguageClick() = Unit
    }

interface SettingsScreenListener {
    fun onCategoryClick(category: String, selected: Boolean)
    fun onStartClick(wordsCount: Int, timerTotalTime: Long)
    fun onLanguageClick()
}