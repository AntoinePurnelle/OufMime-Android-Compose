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

package net.ouftech.oufmime

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.ouftech.oufmime.MainActivity.OufMimeDestinations.PLAY_SCREEN
import net.ouftech.oufmime.MainActivity.OufMimeDestinations.SCOREBOARD_SCREEN
import net.ouftech.oufmime.MainActivity.OufMimeDestinations.SETTINGS_SCREEN
import net.ouftech.oufmime.MainActivity.OufMimeDestinations.TURN_END_SCREEN
import net.ouftech.oufmime.MainActivity.OufMimeDestinations.TURN_START_SCREEN
import net.ouftech.oufmime.MainActivity.OufMimeDestinations.WELCOME_SCREEN
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.WindowSize
import net.ouftech.oufmime.ui.views.rememberWindowSizeClass
import net.ouftech.oufmime.ui.views.screens.*
import net.ouftech.oufmime.utils.LanguageUtils

@SuppressWarnings("LongMethod")
class MainActivity : ComponentActivity() {

    private val vm: WordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.init(application)

        setContent {
            LanguageUtils.updateResources(LocalContext.current)

            val windowSizeClass = rememberWindowSizeClass()
            val backgroundColor = vm.currentTeamColor

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = backgroundColor.toArgb()

            OufMimeTheme(invert = vm.shouldInvertColors) {
                val navController = rememberNavController()
                val isExpandedScreen = windowSizeClass == WindowSize.Expanded
                val dimens = if (isExpandedScreen) ExpandedDimens else MediumDimens

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    NavHost(navController = navController, startDestination = WELCOME_SCREEN) {
                        composable(WELCOME_SCREEN) {
                            WelcomeScreen(
                                dimens = dimens,
                                onStartClick = {
                                    startGame(navController)
                                },
                                onSettingsClick = {
                                    navController.navigate(SETTINGS_SCREEN)
                                }
                            )
                        }

                        composable(TURN_START_SCREEN) {
                            TurnStartScreen(
                                viewModel = vm,
                                dimens = dimens,
                                invertColors = vm.shouldInvertColors
                            ) {
                                vm.initTurn()
                                navController.navigate(PLAY_SCREEN)
                            }
                        }

                        composable(PLAY_SCREEN) {
                            PlayScreen(
                                foundWordsCount = vm.wordsFoundInTurnCount,
                                missedWordsCount = vm.wordsMissedInTurnCount,
                                wordsToPlayCount = vm.wordsToPlayCount,
                                timerMaxValue = vm.timerTotalTime,
                                currentWord = vm.currentWord,
                                dimens = dimens,
                                invertColors = vm.shouldInvertColors,
                                onWordPlayed = { found, timerEnded ->
                                    vm.playWord(found, timerEnded)
                                    if (!vm.hasMoreWords) {
                                        navController.navigate(TURN_END_SCREEN)
                                    }
                                },
                                onFinishTurn = {
                                    navController.navigate(TURN_END_SCREEN)
                                }
                            )
                        }

                        composable(TURN_END_SCREEN) {
                            TurnEndScreen(
                                wordsPlayed = vm.wordsPlayedInTurn,
                                dimens = dimens,
                                isExpandedScreen = isExpandedScreen,
                                invertColors = vm.shouldInvertColors,
                                onWordChange = { changedWord ->
                                    vm.changeValueInPlayedWords(changedWord)
                                },
                                onNextClick = {
                                    vm.finishTurn()

                                    if (vm.hasMoreWords) {
                                        navController.navigate(TURN_START_SCREEN)
                                    } else {
                                        vm.currentRoundFinished = true
                                        navController.navigate(SCOREBOARD_SCREEN)
                                    }
                                }
                            )
                        }

                        composable(SCOREBOARD_SCREEN) {
                            ScoreboardScreen(
                                viewModel = vm,
                                isExpandedScreen = isExpandedScreen,
                                dimens = dimens,
                                onNextClick = {
                                    with(vm) {
                                        if (hasMoreRounds) {
                                            finishRound()
                                            navController.navigate(TURN_START_SCREEN)
                                        } else {
                                            navController.navigate(WELCOME_SCREEN)
                                        }
                                    }
                                }
                            )
                        }

                        composable(SETTINGS_SCREEN) {
                            SettingsScreen(
                                viewModel = vm,
                                dimens = dimens,
                                isExpandedScreen = isExpandedScreen,
                                onStartClick = {
                                    startGame(navController)
                                },
                                onLanguageClick = {
                                    navController.navigate(SETTINGS_SCREEN)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startGame(navController: NavHostController) {
        vm.initGame()
        navController.navigate(TURN_START_SCREEN)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(R.string.quit)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no) { dialogInterface, _ -> dialogInterface.cancel() }
            .show()
    }

    object OufMimeDestinations {
        const val WELCOME_SCREEN = "welcome"
        const val TURN_START_SCREEN = "turnStart"
        const val PLAY_SCREEN = "play"
        const val TURN_END_SCREEN = "turnEnd"
        const val SCOREBOARD_SCREEN = "scoreboard"
        const val SETTINGS_SCREEN = "settings"
    }
}