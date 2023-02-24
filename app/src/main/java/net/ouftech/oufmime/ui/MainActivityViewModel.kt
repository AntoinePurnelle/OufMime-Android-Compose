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

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.data.WordsAccessUseCase
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.Primary
import net.ouftech.oufmime.ui.views.screens.TeamScoreboardUiModel
import net.ouftech.oufmime.ui.views.screens.TurnStartUiModel
import org.koin.java.KoinJavaComponent.inject

@SuppressWarnings("TooManyFunctions")
class MainActivityViewModel : ViewModel() {

    private val insertUseCase: WordsAccessUseCase by inject(WordsAccessUseCase::class.java)

    private var currentTeam by mutableStateOf(-1)
    private var currentRound by mutableStateOf(0)
    var currentRoundFinished by mutableStateOf(true)
    private var words = mutableListOf<Word>()
    private var teamWords: Array<Array<MutableList<Word>>> =
        arrayOf(
            arrayOf(mutableListOf(), mutableListOf(), mutableListOf()),
            arrayOf(mutableListOf(), mutableListOf(), mutableListOf())
        )

    private var wordsToPlay = mutableStateListOf<Word>()
    private var wordsMissedInRound = mutableStateListOf<Word>()
    var wordsPlayedInTurn = mutableStateListOf<Pair<Word, Boolean>>()

    var currentWord by mutableStateOf<Word?>(null)

    var timerTotalTime by mutableStateOf(40000L)
    var wordsCount by mutableStateOf(40)
    var selectedCategories = Categories.values().map { it.name to true }.toMutableStateMap()

    fun init(application: Application) = viewModelScope.launch {
        insertUseCase.insertWords(application.applicationContext)
    }

    // region Game Lifecycle

    fun initGame() {
        runBlocking {
            words = insertUseCase.getRandomWordsInCategories(selectedCategories, wordsCount)
            Log.d("WordsViewModel", "Selected Words (${words.size}) $words")

            currentRound = 0
            currentTeam = 0

            teamWords = arrayOf(
                arrayOf(mutableListOf(), mutableListOf(), mutableListOf()),
                arrayOf(mutableListOf(), mutableListOf(), mutableListOf())
            )

            initRound()
        }
    }

    private fun initRound() {
        currentRoundFinished = false
        wordsToPlay.clear()
        wordsToPlay.addAll(words.shuffled())
        wordsMissedInRound.clear()
        Log.d("WordsViewModel", "Starting round $currentRound - Words to Play (${wordsToPlay.size}): $wordsToPlay")
    }

    fun initTurn() {
        wordsPlayedInTurn.clear()
        currentWord = wordsToPlay.firstOrNull()
    }

    fun playWord(found: Boolean, timerEnded: Boolean) {
        if (hasMoreWords) {
            wordsPlayedInTurn.add(Pair(wordsToPlay.removeFirst(), found))

            Log.d("WordsViewModel", "Word played ${wordsPlayedInTurn.last()}")

            if (!timerEnded) {
                currentWord = wordsToPlay.firstOrNull()
            }
        }
    }

    fun finishTurn() {
        // Add all the words that were found in the Team Round List
        teamWords[currentTeam][currentRound].addAll(wordsPlayedInTurn.filter { it.second }.map { pair -> pair.first })
        // Add all the words that were missed in the Missed List
        wordsToPlay.addAll(wordsPlayedInTurn.filter { !it.second }.map { pair -> pair.first })

        currentTeam = if (currentTeam == 0) 1 else 0

        Log.d("WordsViewModel", "Turned finished and saved")
    }

    fun finishRound() {
        currentRound++
        initRound()
    }

    // endregion Game Lifecycle

    // region Getters/Setters

    fun changeValueInPlayedWords(word: Pair<Word, Boolean>) {
        wordsPlayedInTurn.indexOf(word)
        wordsPlayedInTurn[wordsPlayedInTurn.indexOf(word)] = Pair(first = word.first, second = !word.second)
    }

    val hasMoreWords
        get() = wordsToPlay.size > 0

    val hasMoreRounds
        get() = currentRound < 2

    val wordsFoundInTurnCount
        get() = wordsPlayedInTurn.count { it.second }

    val wordsMissedInTurnCount
        get() = wordsPlayedInTurn.count { !it.second }

    val wordsToPlayCount
        get() = wordsToPlay.size

    private fun getTeamCurrentRoundScore(team: Int) = getTeamRoundScore(team = team, round = currentRound)

    private fun getTeamRoundScore(team: Int, round: Int) = if (currentRound < round) -1 else teamWords[team][round].size

    private fun getTeamTotalScore(team: Int) = teamWords[team].sumOf { it.size }

    val shouldInvertColors
        get() = currentTeam == 0

    private fun getTeamColor(team: Int) = when (team) {
        0 -> Accent
        1 -> Primary
        else -> White
    }

    fun getTeamScoreboardUiModel(team: Int) = TeamScoreboardUiModel(
        color = getTeamColor(team),
        describeScore = getTeamRoundScore(team, 0),
        wordScore = getTeamRoundScore(team, 1),
        mimeScore = getTeamRoundScore(team, 2),
        totalScore = getTeamTotalScore(team)
    )

    fun getTurnStartUiModel() = TurnStartUiModel(
        currentRound = currentRound,
        teamNameId = teamNameId,
        blueTotalScore = getTeamTotalScore(0),
        blueCurrentRoundScore = getTeamCurrentRoundScore(0),
        orangeTotalScore = getTeamTotalScore(1),
        orangeCurrentRoundScore = getTeamCurrentRoundScore(1)
    )

    val currentTeamColor
        get() = if (!currentRoundFinished) getTeamColor(currentTeam) else White

    private val teamNameId
        get() = if (currentTeam == 0) R.string.team_blue else R.string.team_orange

    // endregion Getters/Setters
}