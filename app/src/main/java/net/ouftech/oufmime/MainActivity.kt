package net.ouftech.oufmime

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.OufMimeTheme
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
                    color = colorResource(id = R.color.colorPrimary)
                ) {
                    NavHost(navController = navController, startDestination = WELCOME_SCREEN) {
                        composable(WELCOME_SCREEN) {
                            WelcomeScreen {
                                viewModel.initGame(
                                    categories = Categories.values().map { it.toString() },
                                    wordsCount = 40,
                                    duration = 40000L
                                )
                                navController.navigate(TURN_START_SCREEN)
                            }
                        }

                        composable(TURN_START_SCREEN) {
                            TurnStartScreen(viewModel) {
                                viewModel.initTurn()
                            }
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
        const val TURN_END_SCREEN = "turnEnd"
        const val SCOREBOARD_SCREEN = "scoreboard"
    }
}