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
import net.ouftech.oufmime.ui.views.PlayScreen
import net.ouftech.oufmime.ui.views.TurnEndScreen
import net.ouftech.oufmime.ui.views.TurnStartScreen
import net.ouftech.oufmime.ui.views.WelcomeScreen

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
                                    wordsCount = 5, // TODO pick from settings
                                    duration = 40000L // TODO pick from settings
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
                                timerValue = 22, // TODO Real Values
                                timerMaxValue = 40, // TODO Real Values
                                currentWord = viewModel.currentWord,
                                onWordPlayed = { found ->
                                    viewModel.playWord(found)
                                    if (!viewModel.hasMoreWords()) {
                                        viewModel.finishTurn()
                                        navController.navigate(TURN_END_SCREEN)
                                    }
                                }
                            )
                        }

                        composable(TURN_END_SCREEN) {
                            TurnEndScreen()
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