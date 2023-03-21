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
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()
    private lateinit var dimens: Dimens
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(application)

        setContent {
            LanguageUtils.updateResources(LocalContext.current)

            dimens = if (rememberWindowSizeClass() == WindowSize.Expanded) ExpandedDimens else MediumDimens
            navController = rememberNavController()

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = viewModel.currentTeamColor.toArgb()

            OufMimeTheme(invert = viewModel.shouldInvertColors) {
                Surface(modifier = Modifier.fillMaxSize(), color = viewModel.currentTeamColor) {
                    NavHost(navController = navController, startDestination = WELCOME_SCREEN) {
                        composable(WELCOME_SCREEN) { NavWelcomeScreen() }
                        composable(TURN_START_SCREEN) { NavTurnStartScreen() }
                        composable(PLAY_SCREEN) { NavPlayScreen() }
                        composable(TURN_END_SCREEN) { NavTurnEndScreen() }
                        composable(SCOREBOARD_SCREEN) { NavScoreboardScreen() }
                        composable(SETTINGS_SCREEN) { NavSettingsScreen() }
                    }
                }
            }
        }
    }

    // region Navigation

    @Composable
    private fun NavWelcomeScreen() = WelcomeScreen(
        dimens = dimens,
        onStartClick = { startGame() },
        onSettingsClick = { navController.navigate(SETTINGS_SCREEN) }
    )

    @Composable
    private fun NavTurnStartScreen() = TurnStartScreen(
        model = viewModel.getTurnStartUiModel(),
        dimens = dimens,
        invertColors = viewModel.shouldInvertColors,
        onStartClick = {
            viewModel.initTurn()
            navController.navigate(PLAY_SCREEN)
        }
    )

    @Composable
    private fun NavPlayScreen() = PlayScreen(
        uiModel = viewModel.getPlayScreenUiModel(),
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
    private fun NavTurnEndScreen() = TurnEndScreen(
        wordsPlayed = viewModel.getWordsPlayedInTurn(),
        dimens = dimens,
        invertColors = viewModel.shouldInvertColors,
        onWordChange = { changedWord -> viewModel.changeValueInPlayedWords(changedWord) },
        onNextClick = {
            viewModel.finishTurn()

            if (viewModel.hasMoreWords) {
                navController.navigate(TURN_START_SCREEN)
            } else {
                viewModel.finishRound()
                navController.navigate(SCOREBOARD_SCREEN)
            }
        }
    )

    @Composable
    private fun NavScoreboardScreen() = ScoreboardScreen(
        dimens = dimens,
        teamBlueScoreboardUiModel = viewModel.getTeamScoreboardUiModel(0),
        teamOrangeScoreboardUiModel = viewModel.getTeamScoreboardUiModel(1),
        hasMoreRounds = viewModel.hasMoreRounds,
        onNextClick = {
            with(viewModel) {
                if (hasMoreRounds) {
                    startNextRound()
                    navController.navigate(TURN_START_SCREEN)
                } else {
                    navController.navigate(WELCOME_SCREEN)
                }
            }
        }
    )

    @Composable
    private fun NavSettingsScreen() = SettingsScreen(
        dimens = dimens,
        selectedCategories = viewModel.getSelectedCategories(),
        wordsCount = viewModel.getWordsCount(),
        timerTotalTime = viewModel.getTimerTotalTime() / 1000,
        listener = object : SettingsScreenListener {

            override fun onCategoryClick(category: String, selected: Boolean) {
                viewModel.setCategorySelected(category, selected)
            }

            override fun onStartClick(wordsCount: Int, timerTotalTime: Long) {
                viewModel.setWordsCount(wordsCount)
                viewModel.setTimerTotalTime(timerTotalTime * 1000)
                startGame()
            }

            override fun onLanguageClick() {
                navController.navigate(SETTINGS_SCREEN)
            }

        },
    )

    // endregion Navigation

    private fun startGame() {
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
        private const val WELCOME_SCREEN = "nav:welcome"
        private const val TURN_START_SCREEN = "nav:turnStart"
        private const val PLAY_SCREEN = "nav:play"
        private const val TURN_END_SCREEN = "nav:turnEnd"
        private const val SCOREBOARD_SCREEN = "nav:scoreboard"
        private const val SETTINGS_SCREEN = "nav:settings"
    }

}