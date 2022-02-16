package net.ouftech.oufmime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.ouftech.oufmime.OufMimeDestinations.PLAY_SCREEN
import net.ouftech.oufmime.OufMimeDestinations.SCOREBOARD_SCREEN
import net.ouftech.oufmime.OufMimeDestinations.TURN_END_SCREEN
import net.ouftech.oufmime.OufMimeDestinations.TURN_START_SCREEN
import net.ouftech.oufmime.OufMimeDestinations.WELCOME_SCREEN
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.theme.Primary
import net.ouftech.oufmime.ui.views.*

@Composable
fun OufMimeApp(vm: WordsViewModel, windowSize: WindowSize) {

    OufMimeTheme {
        val navController = rememberNavController()
        val isExpandedScreen = windowSize == WindowSize.Expanded
        val dimens = if (isExpandedScreen) ExpandedDimens else MediumDimens

        // A surface container using the "background" color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Primary
        ) {
            NavHost(navController = navController, startDestination = WELCOME_SCREEN) {
                composable(WELCOME_SCREEN) {
                    WelcomeScreen {
                        vm.initGame(
                            categories = Categories.values().map { it.toString() },
                            duration = 10000L // TODO pick from settings
                        )

                        navController.navigate(TURN_START_SCREEN)
                    }
                }

                composable(TURN_START_SCREEN) {
                    TurnStartScreen(vm, dimens) {
                        vm.initTurn()

                        navController.navigate(PLAY_SCREEN)
                    }
                }

                composable(PLAY_SCREEN) {
                    PlayScreen(
                        foundWordsCount = vm.getWordsFoundInTurnCount(),
                        missedWordsCount = vm.getWordsMissedInTurnCount(),
                        timerMaxValue = vm.timerTotalTime,
                        currentWord = vm.currentWord,
                        dimens = dimens,
                        onWordPlayed = { found, timerEnded ->
                            vm.playWord(found, timerEnded)
                            if (!vm.hasMoreWords()) {
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
                        onWordChange = { changedWord ->
                            vm.changeValueInPlayedWords(changedWord)
                        },
                        onNextClick = {
                            vm.finishTurn()

                            if (vm.hasMoreWords())
                                navController.navigate(TURN_START_SCREEN)
                            else
                                navController.navigate(SCOREBOARD_SCREEN)
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
                                if (currentRound == 2) {
                                    navController.navigate(WELCOME_SCREEN)
                                } else {
                                    currentRound++
                                    initRound()
                                    navController.navigate(TURN_START_SCREEN)
                                }
                            }
                        })
                }
            }
        }
    }
}

object OufMimeDestinations {
    const val WELCOME_SCREEN = "welcome"
    const val TURN_START_SCREEN = "turnStart"
    const val PLAY_SCREEN = "play"
    const val TURN_END_SCREEN = "turnEnd"
    const val SCOREBOARD_SCREEN = "scoreboard"
}