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