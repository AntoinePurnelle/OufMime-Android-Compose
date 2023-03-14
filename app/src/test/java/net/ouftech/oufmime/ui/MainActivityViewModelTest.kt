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

import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.GameData
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.data.WordsAccessUseCase
import net.ouftech.oufmime.utils.Logger
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class MainActivityViewModelTest {

    @MockK private lateinit var wordsAccessUseCase: WordsAccessUseCase
    @MockK private lateinit var logger: Logger

    private lateinit var vm: MainActivityViewModel
    private var gameData = GameData()

    @Before
    fun setup() {
        wordsAccessUseCase = mockk()
        logger = mockk()
        vm = MainActivityViewModel(wordsAccessUseCase, logger)
        vm.replaceGameData(gameData)
    }

    private fun skipGameStart(wordsCount: Int = 40) {
        gameData.wordsCount = wordsCount
        coEvery { wordsAccessUseCase.getRandomWordsInCategories(any(), wordsCount) } returns getWordsList(wordsCount)
        every { logger.d(any()) } just Runs
        vm.initGame()
        clearAllMocks()
    }

    private fun skipTurnStart(wordsCount: Int = 40) {
        skipGameStart(wordsCount)
        vm.initTurn()
    }

    @Test
    fun initGame() {
        // GIVEN
        val wordsCount = 10
        val words = getWordsList(wordsCount)

        gameData.wordsCount = wordsCount
        coEvery { wordsAccessUseCase.getRandomWordsInCategories(any(), wordsCount) } returns words
        every { logger.d(any()) } just Runs

        // WHEN
        vm.initGame()

        // THEN
        coVerify {
            wordsAccessUseCase.getRandomWordsInCategories(any(), wordsCount)
        }

        Truth.assertThat(gameData.words).isEqualTo(words)
        Truth.assertThat(gameData.wordsToPlay.size).isEqualTo(wordsCount)
        Truth.assertThat(gameData.currentRound).isEqualTo(0)
        Truth.assertThat(gameData.currentTeam).isEqualTo(0)
        Truth.assertThat(gameData.teamWords).isEqualTo(getEmptyTeamWords())
        Truth.assertThat(gameData.wordsMissedInRound).isEmpty()
        Truth.assertThat(gameData.currentRoundFinished).isFalse()
    }

    @Test
    fun initTurn() {
        // GIVEN
        skipGameStart()
        every { logger.d(any()) } just Runs

        // WHEN
        vm.initTurn()

        // THEN
        Truth.assertThat(gameData.wordsPlayedInTurn).isEmpty()
        Truth.assertThat(gameData.currentWord).isEqualTo(gameData.wordsToPlay.first())
    }

    @Test
    @Parameters(method = "playWordParams")
    fun playWord(hasMoreWords: Boolean, found: Boolean, timerEnded: Boolean) {
        // GIVEN
        skipTurnStart()
        if (!hasMoreWords) {
            gameData.wordsToPlay.clear()
        }
        val prevWordsPlayedInTurnCount = gameData.wordsPlayedInTurn.size
        val prevWordsToPlayCount = gameData.wordsToPlay.size
        val wordPlayed = gameData.wordsToPlay.firstOrNull()
        val prevCurrentWord = gameData.currentWord
        every { logger.d(any()) } just Runs

        // WHEN
        vm.playWord(found, timerEnded)

        // THEN
        if (hasMoreWords) {
            Truth.assertThat(gameData.wordsPlayedInTurn.last()).isEqualTo(Pair(wordPlayed, found))
            Truth.assertThat(gameData.wordsToPlay).isNotEqualTo(wordPlayed)
            Truth.assertThat(gameData.wordsPlayedInTurn.size).isEqualTo(prevWordsPlayedInTurnCount + 1)
            Truth.assertThat(gameData.wordsToPlay.size).isEqualTo(prevWordsToPlayCount - 1)
            if (timerEnded) {
                Truth.assertThat(gameData.currentWord).isEqualTo(prevCurrentWord)
            } else {
                Truth.assertThat(gameData.currentWord).isEqualTo(gameData.wordsToPlay.firstOrNull())
            }
        }
    }

    @Test
    fun finishTurn() {
        // GIVEN
        skipTurnStart()
        every { logger.d(any()) } just Runs
        val prevWordsToPlayCount = gameData.wordsToPlay.size
        val first = gameData.wordsToPlay.first()
        vm.playWord(found = true, timerEnded = false)
        val second = gameData.wordsToPlay.first()
        vm.playWord(found = false, timerEnded = false)

        // WHEN
        vm.finishTurn()

        // THEN
        Truth.assertThat(gameData.teamWords[0][0].size).isEqualTo(1)
        Truth.assertThat(gameData.teamWords[0][0]).contains(first)
        Truth.assertThat(gameData.teamWords[0][0]).doesNotContain(second)
        Truth.assertThat(gameData.wordsToPlay.size).isEqualTo(prevWordsToPlayCount - 1)
        Truth.assertThat(gameData.currentTeam).isEqualTo(1)
    }

    @Test
    fun finishRound() {
        // GIVEN
        skipTurnStart()

        // WHEN
        vm.finishRound()

        // THEN
        Truth.assertThat(gameData.currentRoundFinished).isTrue()
    }

    @Test
    fun startNextRound() {
        // GIVEN
        val wordsCount = 20
        skipTurnStart(wordsCount)
        vm.finishRound()
        every { logger.d(any()) } just Runs

        // WHEN
        vm.startNextRound()

        // THEN
        Truth.assertThat(gameData.currentRound).isEqualTo(1)
        Truth.assertThat(gameData.wordsMissedInRound).isEmpty()
        Truth.assertThat(gameData.currentRoundFinished).isFalse()
        Truth.assertThat(gameData.wordsToPlay.size).isEqualTo(wordsCount)
    }

    @Test
    fun changeValueInPlayedWords() {
        // GIVEN
        skipTurnStart()
        every { logger.d(any()) } just Runs
        val first = gameData.wordsToPlay.first()
        vm.playWord(found = true, timerEnded = false)
        val second = gameData.wordsToPlay.first()
        vm.playWord(found = false, timerEnded = false)

        // WHEN
        vm.changeValueInPlayedWords(Pair(first, true))
        vm.changeValueInPlayedWords(Pair(second, false))

        // THEN
        Truth.assertThat(gameData.wordsPlayedInTurn.size).isEqualTo(2)
        Truth.assertThat(gameData.wordsPlayedInTurn[0].second).isFalse()
        Truth.assertThat(gameData.wordsPlayedInTurn[1].second).isTrue()
    }

    private fun getWordsList(count: Int) = (1..count).map {
        Word(it.toString(), Categories.values().random(), "en")
    }.toMutableList()


    private fun getEmptyTeamWords() = arrayOf(
        arrayOf(mutableListOf<Word>(), mutableListOf(), mutableListOf()),
        arrayOf(mutableListOf(), mutableListOf(), mutableListOf())
    )

    @Suppress("Unused")
    private fun playWordParams() = listOf(
        listOf(true, true, true),
        listOf(true, false, true),
        listOf(true, false, false),
        listOf(true, true, false),
        listOf(false, true, true),
        listOf(false, false, true),
        listOf(false, false, false),
        listOf(false, true, false),
    )

}