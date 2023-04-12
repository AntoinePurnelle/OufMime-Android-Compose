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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word

class GameData {
    var currentTeam by mutableStateOf(-1)
    var currentRound by mutableStateOf(0)
    var currentRoundFinished by mutableStateOf(true)
    var words = mutableListOf<Word>()
    var teamWords: Array<Array<MutableList<Word>>> =
        arrayOf(
            arrayOf(mutableListOf(), mutableListOf(), mutableListOf()),
            arrayOf(mutableListOf(), mutableListOf(), mutableListOf())
        )

    var wordsToPlay = mutableStateListOf<Word>()
    var wordsMissedInRound = mutableStateListOf<Word>()
    var wordsPlayedInTurn = mutableStateListOf<Pair<Word, Boolean>>()

    var currentWord by mutableStateOf<Word?>(null)

    var timerTotalTime by mutableStateOf(40000L)
    var wordsCount by mutableStateOf(40)
    var selectedCategories = Categories.values().map { it.name to true }.toMutableStateMap()

    fun resetWordsToPlay() {
        wordsToPlay.clear()
        wordsToPlay.addAll(words.shuffled())
    }

    val hasMoreRounds
        get() = currentRound < 2
}