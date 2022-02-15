package net.ouftech.oufmime

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.theme.Primary
import net.ouftech.oufmime.ui.views.*

class MainActivity : ComponentActivity() {

    private val viewModel: WordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(application)

        setContent {
            OufMimeTheme {
                val navController = rememberNavController()
                // A surface container using the "background" color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Primary
                ) {
                    NavHost(navController = navController, startDestination = WELCOME_SCREEN) {
                        composable(WELCOME_SCREEN) {
                            WelcomeScreen {
                                viewModel.initGame(
                                    categories = Categories.values().map { it.toString() },
                                    duration = 10000L // TODO pick from settings
                                )

                                navController.navigate(TURN_START_SCREEN)
                            }
                        }

                        composable(TURN_START_SCREEN) {
                            TurnStartScreen(viewModel) {
                                viewModel.initTurn()

                                navController.navigate(PLAY_SCREEN)
                            }
                        }

                        composable(PLAY_SCREEN) {
                            PlayScreen(
                                foundWordsCount = viewModel.getWordsFoundInTurnCount(),
                                missedWordsCount = viewModel.getWordsMissedInTurnCount(),
                                timerMaxValue = viewModel.timerTotalTime,
                                currentWord = viewModel.currentWord,
                                onWordPlayed = { found ->
                                    viewModel.playWord(found)
                                    if (!viewModel.hasMoreWords()) {
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
                                wordsPlayed = viewModel.wordsPlayedInTurn,
                                onWordChange = { changedWord ->
                                    viewModel.changeValueInPlayedWords(changedWord)
                                },
                                onNextClick = {
                                    viewModel.finishTurn()

                                    if (viewModel.hasMoreWords())
                                        navController.navigate(TURN_START_SCREEN)
                                    else
                                        navController.navigate(SCOREBOARD_SCREEN)
                                }
                            )
                        }

                        composable(SCOREBOARD_SCREEN) {
                            ScoreboardScreen(viewModel = viewModel, onNextClick = {
                                with(viewModel) {
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
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(R.string.quit)
            .setPositiveButton(R.string.yes) { _, _ -> finish() }
            .setNegativeButton(R.string.no) { dialogInterface, _ -> dialogInterface.cancel() }
            .show()
    }

    companion object {
        const val WELCOME_SCREEN = "welcome"
        const val TURN_START_SCREEN = "turnStart"
        const val PLAY_SCREEN = "play"
        const val TURN_END_SCREEN = "turnEnd"
        const val SCOREBOARD_SCREEN = "scoreboard"
    }
}