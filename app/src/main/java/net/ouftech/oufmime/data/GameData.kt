package net.ouftech.oufmime.data

import androidx.compose.runtime.*

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
}