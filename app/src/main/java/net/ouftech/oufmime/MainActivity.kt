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
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.RoundStartView

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
                    NavHost(navController = navController, startDestination = ROUND_START_VIEW) {
                        composable(ROUND_START_VIEW) {
                            RoundStartView(viewModel) {
                                viewModel.team1TotalScore =
                                    viewModel.team1TotalScore + 1 // TODO Switch screen instead
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
        const val ROUND_START_VIEW = "roundStart"
        const val ROUND_END_SCREEN = "roundEnd"
        const val TURN_END_SCREEN = "turnEnd"
    }
}