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
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.BlueTeam
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OrangeTeam
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.WindowSize
import net.ouftech.oufmime.ui.views.rememberWindowSizeClass
import net.ouftech.oufmime.ui.views.screens.PlayScreen
import net.ouftech.oufmime.ui.views.screens.ScoreboardScreen
import net.ouftech.oufmime.ui.views.screens.SettingsScreen
import net.ouftech.oufmime.ui.views.screens.SettingsScreenListener
import net.ouftech.oufmime.ui.views.screens.TurnEndScreen
import net.ouftech.oufmime.ui.views.screens.TurnStartScreen
import net.ouftech.oufmime.ui.views.screens.WelcomeScreen
import net.ouftech.oufmime.utils.LanguageUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(application)

        setContent {
            LanguageUtils.updateResources(LocalContext.current)

            viewModel.updateDimens(if (rememberWindowSizeClass() == WindowSize.Expanded) ExpandedDimens else MediumDimens)
            navController = rememberNavController()

            hideSystemUI()

            OufMimeTheme(viewModel.colorPalette) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    if (viewModel.colorPalette == -1) {
                        NoTeamBackground()
                    }

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

    @Composable
    private fun NoTeamBackground() = Canvas(modifier = Modifier.fillMaxSize()) {
        if (viewModel.dimens.isExpandedScreen) {
            val midScreen = size.width / 2
            drawRect(color = OrangeTeam, size = size.copy(width = midScreen))
            drawRect(color = BlueTeam, topLeft = Offset(y = 0f, x = midScreen), size = size.copy(width = midScreen))
        } else {
            val midScreen = size.height / 2
            drawRect(color = OrangeTeam, size = size.copy(height = midScreen))
            drawRect(color = BlueTeam, topLeft = Offset(y = midScreen, x = 0f), size = size.copy(height = midScreen))
        }
    }

    private fun hideSystemUI() {
        // Hides the ugly action bar at the top
        actionBar?.hide()

        // Hides the status bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
        }
    }

    // region Navigation

    @Composable
    private fun NavWelcomeScreen() = WelcomeScreen(
        dimens = viewModel.dimens,
        onStartClick = { startGame() },
        onSettingsClick = { navController.navigate(SETTINGS_SCREEN) }
    )

    @Composable
    private fun NavTurnStartScreen() = TurnStartScreen(
        model = viewModel.getTurnStartUiState(),
        onStartClick = {
            viewModel.initTurn()
            navController.navigate(PLAY_SCREEN)
        }
    )

    @Composable
    private fun NavPlayScreen() = PlayScreen(
        uiState = viewModel.getPlayScreenUiState(),
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
        model = viewModel.getTurnEndUiState(),
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
        uiState = viewModel.getScoreboardScreenUiState(),
        onNextClick = {
            if (viewModel.hasMoreRounds) {
                viewModel.startNextRound()
                navController.navigate(TURN_START_SCREEN)
            } else {
                navController.navigate(WELCOME_SCREEN)
            }
        }
    )

    @Composable
    private fun NavSettingsScreen() = SettingsScreen(
        uiState = viewModel.getSettingsScreenUiState(),
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