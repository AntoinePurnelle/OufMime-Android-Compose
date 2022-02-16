package net.ouftech.oufmime.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import net.ouftech.oufmime.data.Categories.*
import java.io.IOException

class WordsViewModel : ViewModel() {

    private var repository: WordsRepository? = null
    var currentTeam by mutableStateOf(0)
    var currentRound by mutableStateOf(0)
    var words = mutableListOf<Word>()
    private var teamWords: Array<Array<MutableList<Word>>> =
        arrayOf(
            arrayOf(mutableListOf(), mutableListOf(), mutableListOf()),
            arrayOf(mutableListOf(), mutableListOf(), mutableListOf())
        )

    var wordsToPlay = mutableStateListOf<Word>()
    private var wordsMissedInRound = mutableStateListOf<Word>()
    var wordsPlayedInTurn = mutableStateListOf<Pair<Word, Boolean>>()

    var currentWord by mutableStateOf<Word?>(null)

    var timerTotalTime by mutableStateOf(10000L)
    var timerCurrentTime by mutableStateOf(timerTotalTime)
    var wordsCount by mutableStateOf(5)

    // region Startup

    fun init(application: Application) {
        if (repository == null) {
            val db = WordsDB.getDataBase(application)
            repository = WordsRepository(db.wordsDao())
            insertWords(application.applicationContext)
        }
    }

    private fun insertWords(context: Context) {
        runBlocking {
            val jsonFileString = getJsonDataFromAsset(context, "words.json")
            Log.d("data", jsonFileString!!)

            val words = Gson().fromJson(jsonFileString, WordLists::class.java)

            insertListOfWords(ACTIONS, words.actions)
            insertListOfWords(ACTIVITIES, words.activities)
            insertListOfWords(ANATOMY, words.anatomy)
            insertListOfWords(ANIMALS, words.animals)
            insertListOfWords(CELEBRITIES, words.celebrities)
            insertListOfWords(CLOTHES, words.clothes)
            insertListOfWords(EVENTS, words.events)
            insertListOfWords(FICTIONAL, words.fictional)
            insertListOfWords(FOOD, words.food)
            insertListOfWords(LOCATIONS, words.locations)
            insertListOfWords(JOBS, words.jobs)
            insertListOfWords(NATURE, words.nature)
            insertListOfWords(OBJECTS, words.objects)
            insertListOfWords(VEHICLES, words.vehicles)

            Log.d(
                "GameManager",
                "All Words (${repository?.getAllWords()?.size}) ${repository?.getAllWords()}"
            )
        }

    }

    private suspend fun insertListOfWords(
        category: Categories,
        wordsToSort: List<String>
    ) = repository?.insertWords(mutableListOf<Word>().also { words ->
        wordsToSort.forEach {
            words.add(Word(it, category))
        }
    })

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    // endregion Startup

    // region Game

    fun initGame(categories: List<String>, duration: Long) {
        runBlocking {
            words = repository!!.getRandomWordsInCategories(categories, wordsCount).toMutableList()
            timerTotalTime = duration
            Log.d("GameManager", "Selected Words (${words.size}) $words")

            currentRound = 0
            currentTeam = 0

            teamWords = arrayOf(
                arrayOf(mutableListOf(), mutableListOf(), mutableListOf()),
                arrayOf(mutableListOf(), mutableListOf(), mutableListOf())
            )

            initRound()
        }
    }

    fun initRound() {
        wordsToPlay.clear()
        wordsToPlay.addAll(words.shuffled())
        wordsMissedInRound.clear()
        Log.d(
            "GameManager",
            "Starting round $currentRound - Words to Play (${wordsToPlay.size}): $wordsToPlay"
        )
    }

    fun initTurn() {
        wordsPlayedInTurn = mutableStateListOf()
        currentWord = wordsToPlay.firstOrNull()
        timerCurrentTime = timerTotalTime
    }

    fun playWord(found: Boolean, timerEnded: Boolean) {
        if (hasMoreWords()) {
            wordsPlayedInTurn.add(Pair(wordsToPlay.removeFirst(), found))

            Log.d("GameManager", "Word played ${wordsPlayedInTurn.last()}")

            if (!timerEnded)
                currentWord = wordsToPlay.firstOrNull()
        }
    }

    fun finishTurn() {
        // Add all the words that were found in the Team Round List
        teamWords[currentTeam][currentRound].addAll(wordsPlayedInTurn.filter { it.second }
            .map { pair -> pair.first })
        // Add all the words that were missed in the Missed List
        wordsToPlay.addAll(wordsPlayedInTurn.filter { !it.second }.map { pair -> pair.first })

        currentTeam = if (currentTeam == 0) 1 else 0

        Log.d("GameManager", "Turned finished and saved")
    }

    fun changeValueInPlayedWords(word: Pair<Word, Boolean>) {
        wordsPlayedInTurn.indexOf(word)
        wordsPlayedInTurn[wordsPlayedInTurn.indexOf(word)] =
            Pair(first = word.first, second = !word.second)
    }

    fun hasMoreWords() = wordsToPlay.size > 0

    fun getWordsFoundInTurnCount() = wordsPlayedInTurn.count { it.second }
    fun getWordsMissedInTurnCount() = wordsPlayedInTurn.count { !it.second }

    fun getTeamRoundScore(team: Int, round: Int) =
        if (currentRound < round) -1 else teamWords[team][round].size

    fun getTeamTotalScore(team: Int) = teamWords[team].sumOf { it.size }

    // endregion Game
}