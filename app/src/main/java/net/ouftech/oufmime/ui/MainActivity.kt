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

package net.ouftech.oufmime.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.WindowSize
import net.ouftech.oufmime.ui.views.rememberWindowSizeClass
import net.ouftech.oufmime.ui.views.screens.*
import net.ouftech.oufmime.utils.LanguageUtils

class MainActivity : ComponentActivity() {

    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(application)

        setContent {
            LanguageUtils.updateResources(LocalContext.current)

            val windowSizeClass = rememberWindowSizeClass()
            val backgroundColor = viewModel.currentTeamColor

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = backgroundColor.toArgb()

            OufMimeTheme(invert = viewModel.shouldInvertColors) {
                val navController = rememberNavController()
                val isExpandedScreen = windowSizeClass == WindowSize.Expanded
                val dimens = if (isExpandedScreen) ExpandedDimens else MediumDimens

                Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
                    Nav(dimens, isExpandedScreen, navController)
                }
            }
        }
    }

    // region Navigation

    @Composable
    private fun Nav(dimens: Dimens, isExpandedScreen: Boolean, navController: NavHostController) =
        NavHost(navController = navController, startDestination = WELCOME_SCREEN) {
            composable(WELCOME_SCREEN) { NavWelcomeScreen(dimens, navController) }
            composable(TURN_START_SCREEN) { NavTurnStartScreen(dimens, navController) }
            composable(PLAY_SCREEN) { NavPlayScreen(dimens, navController) }
            composable(TURN_END_SCREEN) { NavTurnEndScreen(dimens, isExpandedScreen, navController) }
            composable(SCOREBOARD_SCREEN) { NavScoreboardScreen(dimens, isExpandedScreen, navController) }
            composable(SETTINGS_SCREEN) { NavSettingsScreen(dimens, isExpandedScreen, navController) }
        }

    @Composable
    private fun NavWelcomeScreen(dimens: Dimens, navController: NavHostController) = WelcomeScreen(
        dimens = dimens,
        onStartClick = { startGame(navController, viewModel) },
        onSettingsClick = { navController.navigate(SETTINGS_SCREEN) }
    )

    @Composable
    private fun NavTurnStartScreen(dimens: Dimens, navController: NavHostController) =
        TurnStartScreen(dimens, viewModel.shouldInvertColors, viewModel.getTurnStartUiModel()) {
            viewModel.initTurn()
            navController.navigate(PLAY_SCREEN)
        }

    @Composable
    private fun NavPlayScreen(dimens: Dimens, navController: NavHostController) =
        PlayScreen(
            foundWordsCount = viewModel.wordsFoundInTurnCount,
            missedWordsCount = viewModel.wordsMissedInTurnCount,
            wordsToPlayCount = viewModel.wordsToPlayCount,
            timerMaxValue = viewModel.timerTotalTime,
            currentWord = viewModel.currentWord,
            dimens = dimens,
            invertColors = viewModel.shouldInvertColors,
            onWordPlayed = { found, timerEnded ->
                viewModel.playWord(found, timerEnded)
                if (!viewModel.hasMoreWords) {
                    navController.navigate(TURN_END_SCREEN)
                }
            },
            onFinishTurn = {
                navController.navigate(TURN_END_SCREEN)
            }
        )

    @Composable
    private fun NavTurnEndScreen(dimens: Dimens, isExpandedScreen: Boolean, navController: NavHostController) =
        TurnEndScreen(
            wordsPlayed = viewModel.wordsPlayedInTurn,
            dimens = dimens,
            isExpandedScreen = isExpandedScreen,
            invertColors = viewModel.shouldInvertColors,
            onWordChange = { changedWord -> viewModel.changeValueInPlayedWords(changedWord) },
            onNextClick = {
                viewModel.finishTurn()

                if (viewModel.hasMoreWords) {
                    navController.navigate(TURN_START_SCREEN)
                } else {
                    viewModel.currentRoundFinished = true
                    navController.navigate(SCOREBOARD_SCREEN)
                }
            }
        )

    @Composable
    private fun NavScoreboardScreen(dimens: Dimens, isExpandedScreen: Boolean, navController: NavHostController) =
        ScoreboardScreen(
            isExpandedScreen = isExpandedScreen,
            dimens = dimens,
            teamBlueScoreboardUiModel = viewModel.getTeamScoreboardUiModel(0),
            teamOrangeScoreboardUiModel = viewModel.getTeamScoreboardUiModel(1),
            hasMoreRounds = viewModel.hasMoreRounds,
            onNextClick = {
                with(viewModel) {
                    if (hasMoreRounds) {
                        finishRound()
                        navController.navigate(TURN_START_SCREEN)
                    } else {
                        navController.navigate(WELCOME_SCREEN)
                    }
                }
            }
        )

    @Composable
    private fun NavSettingsScreen(dimens: Dimens, isExpandedScreen: Boolean, navController: NavHostController) =
        SettingsScreen(
            dimens = dimens,
            isExpandedScreen = isExpandedScreen,
            selectedCategories = viewModel.selectedCategories,
            wordsCount = viewModel.wordsCount,
            timerTotalTime = viewModel.timerTotalTime / 1000,
            listener = object : SettingsScreenListener {

                override fun onCategoryClick(category: String, selected: Boolean) {
                    viewModel.selectedCategories[category] = selected
                }

                override fun onStartClick(wordsCount: Int, timerTotalTime: Long) {
                    viewModel.wordsCount = wordsCount
                    viewModel.timerTotalTime = timerTotalTime * 1000
                    startGame(navController, viewModel)
                }

                override fun onLanguageClick() {
                    navController.navigate(SETTINGS_SCREEN)
                }

            },
        )

    // endregion Navigation

    private fun startGame(navController: NavHostController, viewModel: MainActivityViewModel) {
        viewModel.initGame()
        navController.navigate(TURN_START_SCREEN)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(R.string.quit)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no) { dialogInterface, _ -> dialogInterface.cancel() }
            .show()
    }

    companion object {
        private const val WELCOME_SCREEN = "welcome"
        private const val TURN_START_SCREEN = "turnStart"
        private const val PLAY_SCREEN = "play"
        private const val TURN_END_SCREEN = "turnEnd"
        private const val SCOREBOARD_SCREEN = "scoreboard"
        private const val SETTINGS_SCREEN = "settings"
    }

}