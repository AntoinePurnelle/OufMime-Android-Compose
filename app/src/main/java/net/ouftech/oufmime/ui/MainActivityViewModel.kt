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
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.GameData
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.data.WordsAccessUseCase
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.Primary
import net.ouftech.oufmime.ui.views.screens.PlayScreenUiModel
import net.ouftech.oufmime.ui.views.screens.TeamScoreboardUiModel
import net.ouftech.oufmime.ui.views.screens.TurnStartUiModel
import net.ouftech.oufmime.utils.Logger

@SuppressWarnings("TooManyFunctions")
class MainActivityViewModel(
    private val wordsAccessUseCase: WordsAccessUseCase,
    private val logger: Logger
) : ViewModel() {

    private var gameData = GameData()

    fun init(application: Application) = viewModelScope.launch {
        wordsAccessUseCase.insertWords(application.applicationContext)
    }

    // region Game Lifecycle

    fun initGame() = with(gameData) {
        runBlocking {
            words = wordsAccessUseCase.getRandomWordsInCategories(selectedCategories, wordsCount)
            logger.d("Selected Words (${words.size}) $words")

            currentRound = 0
            currentTeam = 0

            teamWords = arrayOf(
                arrayOf(mutableListOf(), mutableListOf(), mutableListOf()),
                arrayOf(mutableListOf(), mutableListOf(), mutableListOf())
            )

            initRound()
        }
    }

    private fun initRound() = with(gameData) {
        currentRoundFinished = false
        resetWordsToPlay()
        wordsMissedInRound.clear()
        logger.d("Starting round $currentRound - Words to Play (${wordsToPlay.size}): $wordsToPlay")
    }

    fun initTurn() = with(gameData) {
        wordsPlayedInTurn.clear()
        currentWord = wordsToPlay.firstOrNull()
    }

    fun playWord(found: Boolean, timerEnded: Boolean) = with(gameData) {
        if (hasMoreWords) {
            wordsPlayedInTurn.add(Pair(wordsToPlay.removeFirst(), found))

            logger.d("Word played ${wordsPlayedInTurn.last()}")

            if (!timerEnded) {
                currentWord = wordsToPlay.firstOrNull()
            }
        }
    }

    fun finishTurn() = with(gameData) {
        // Add all the words that were found in the Team Round List
        teamWords[currentTeam][currentRound].addAll(wordsPlayedInTurn.filter { it.second }.map { pair -> pair.first })
        // Add all the words that were missed in the Missed List
        wordsToPlay.addAll(wordsPlayedInTurn.filter { !it.second }.map { pair -> pair.first })

        currentTeam = if (currentTeam == 0) 1 else 0

        logger.d("Turned finished and saved")
    }

    fun finishRound() = with(gameData) {
        currentRoundFinished = true
    }

    fun startNextRound() = with(gameData) {
        currentRound++
        initRound()
    }

    fun changeValueInPlayedWords(word: Pair<Word, Boolean>) = with(gameData) {
        wordsPlayedInTurn[wordsPlayedInTurn.indexOf(word)] = Pair(first = word.first, second = !word.second)
    }

    // endregion Game Lifecycle

    // region Getters/Setters

    fun getWordsPlayedInTurn() = gameData.wordsPlayedInTurn

    fun getTimerTotalTime() = gameData.timerTotalTime

    fun setTimerTotalTime(time: Long) {
        gameData.timerTotalTime = time
    }

    fun getSelectedCategories() = gameData.selectedCategories

    fun setCategorySelected(category: String, selected: Boolean) {
        gameData.selectedCategories[category] = selected
    }

    fun getWordsCount() = gameData.wordsCount

    fun setWordsCount(wordsCount: Int) {
        gameData.wordsCount = wordsCount
    }

    val hasMoreWords
        get() = gameData.wordsToPlay.size > 0

    val hasMoreRounds
        get() = gameData.currentRound < 2

    private fun getTeamCurrentRoundScore(team: Int) = getTeamRoundScore(team = team, round = gameData.currentRound)

    private fun getTeamRoundScore(team: Int, round: Int) = if (gameData.currentRound < round) -1 else gameData.teamWords[team][round].size

    private fun getTeamTotalScore(team: Int) = gameData.teamWords[team].sumOf { it.size }

    val shouldInvertColors
        get() = gameData.currentTeam == 0

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
        currentRound = gameData.currentRound,
        teamNameId = if (gameData.currentTeam == 0) R.string.team_blue else R.string.team_orange,
        blueTotalScore = getTeamTotalScore(0),
        blueCurrentRoundScore = getTeamCurrentRoundScore(0),
        orangeTotalScore = getTeamTotalScore(1),
        orangeCurrentRoundScore = getTeamCurrentRoundScore(1)
    )

    fun getPlayScreenUiModel() = PlayScreenUiModel(
        foundWordsCount = gameData.wordsPlayedInTurn.count { it.second },
        missedWordsCount = gameData.wordsPlayedInTurn.count { !it.second },
        wordsToPlayCount = gameData.wordsToPlay.size,
        timerMaxValue = getTimerTotalTime(),
        currentWord = gameData.currentWord,
    )

    val currentTeamColor
        get() = if (!gameData.currentRoundFinished) getTeamColor(gameData.currentTeam) else White

    @VisibleForTesting
    fun replaceGameData(gameData: GameData) {
        this.gameData = gameData
    }

    // endregion Getters/Setters
}