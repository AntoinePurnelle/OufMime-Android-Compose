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

package net.ouftech.oufmime.ui.model

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.data.usecases.GetRandomWordsInCategoriesUseCase
import net.ouftech.oufmime.data.usecases.InsertWordsUseCase
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.utils.Logger

@SuppressWarnings("TooManyFunctions")
class MainActivityViewModel(
    private val insertWordsUseCase: InsertWordsUseCase,
    private val getRandomWordsInCategoriesUseCase: GetRandomWordsInCategoriesUseCase,
    private val transformer: GameDataToUiStateTransformer,
    private val logger: Logger,
) : ViewModel() {

    private var gameData = GameData()
    var dimens: Dimens = Dimens()
        private set

    fun init(application: Application) = viewModelScope.launch {
        insertWordsUseCase(application.applicationContext)
    }

    // region Game Lifecycle

    fun initGame() = with(gameData) {
        runBlocking {
            words = getRandomWordsInCategoriesUseCase(selectedCategories, wordsCount).words.toMutableList()
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

    fun setTimerTotalTime(time: Long) {
        gameData.timerTotalTime = time
    }

    fun setCategorySelected(category: String, selected: Boolean) {
        gameData.selectedCategories[category] = selected
    }

    fun setWordsCount(wordsCount: Int) {
        gameData.wordsCount = wordsCount
    }

    val hasMoreWords
        get() = gameData.wordsToPlay.size > 0

    val hasMoreRounds
        get() = gameData.hasMoreRounds

    val colorPalette: Int
        get() = when {
            gameData.currentRoundFinished -> -1
            else -> gameData.currentTeam
        }

    fun getTurnStartUiState() = transformer.getTurnStartUiState(gameData, dimens)

    fun getPlayScreenUiState() = transformer.getPlayScreenUiState(gameData, dimens)

    fun getTurnEndUiState() = transformer.getTurnEndUiState(gameData, dimens)

    fun getScoreboardScreenUiState() = transformer.getScoreboardScreenUiState(gameData, dimens)

    fun getSettingsScreenUiState() = transformer.getSettingsScreenUiState(gameData, dimens)
    fun updateDimens(dimens: Dimens) {
        this.dimens = dimens
    }

    @VisibleForTesting
    fun replaceGameData(gameData: GameData) {
        this.gameData = gameData
    }

    // endregion Getters/Setters
}