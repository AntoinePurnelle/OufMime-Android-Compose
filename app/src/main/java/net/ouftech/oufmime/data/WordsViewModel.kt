package net.ouftech.oufmime.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories.*
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.Primary
import java.io.IOException
import java.util.*

class WordsViewModel : ViewModel() {

    private var repository: WordsRepository? = null
    lateinit var dataStoreManager: DataStoreManager

    private var currentTeam by mutableStateOf(-1)
    var currentRound by mutableStateOf(0)
    var currentRoundFinished by mutableStateOf(true)
    var words = mutableListOf<Word>()
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
    var timerCurrentTime by mutableStateOf(timerTotalTime)
    var wordsCount by mutableStateOf(40)
    var selectedCategories = Categories.values().map { it.name to true }.toMutableStateMap()


    fun init(application: Application) {
        if (repository == null) {
            dataStoreManager = DataStoreManager(application)

            val db = WordsDB.getDataBase(application)
            repository = WordsRepository(db.wordsDao())
            insertWords(application.applicationContext)
        }
    }

    // region DB Access
    private fun insertWords(context: Context) {
        runBlocking {
            val jsonFileString = getJsonDataFromAsset(context, "words.json")
            Log.d("data", jsonFileString!!)

            val words = Gson().fromJson(jsonFileString, WordLists::class.java)

            GlobalScope.launch(
                Dispatchers.IO
            ) {
                dataStoreManager.getWordsListVersion().catch { e ->
                    Log.e("GameManager", e.toString())
                }.collect {
                    if (it < words.version) {
                        insertLanguageWords("en", words.en)
                        insertLanguageWords("fr", words.fr)

                        GlobalScope.launch(Dispatchers.IO) {
                            dataStoreManager.saveWordsListsVersion(words.version)
                        }
                    }

                    val allWords = repository?.getAllWords()

                    Log.d(
                        "GameManager",
                        "All Words (${allWords?.size}) $allWords"
                    )
                }
            }
        }
    }

    private suspend fun insertLanguageWords(language: String, words: TranslatedWords) {
        insertListOfWords(ACTIONS, words.actions, language)
        insertListOfWords(ACTIVITIES, words.activities, language)
        insertListOfWords(ANATOMY, words.anatomy, language)
        insertListOfWords(ANIMALS, words.animals, language)
        insertListOfWords(CELEBRITIES, words.celebrities, language)
        insertListOfWords(CLOTHES, words.clothes, language)
        insertListOfWords(EVENTS, words.events, language)
        insertListOfWords(FICTIONAL, words.fictional, language)
        insertListOfWords(FOOD, words.food, language)
        insertListOfWords(GEEK, words.geek, language)
        insertListOfWords(LOCATIONS, words.locations, language)
        insertListOfWords(JOBS, words.jobs, language)
        insertListOfWords(MYTHOLOGY, words.mythology, language)
        insertListOfWords(NATURE, words.nature, language)
        insertListOfWords(OBJECTS, words.objects, language)
        insertListOfWords(VEHICLES, words.vehicles, language)
    }

    private suspend fun insertListOfWords(
        category: Categories,
        wordsToSort: List<String>,
        language: String
    ) = repository?.insertWords(mutableListOf<Word>().also { words ->
        wordsToSort.forEach {
            words.add(Word(it, category, language))
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

    // endregion DB Access

    // region Game Lifecycle

    fun initGame() {
        runBlocking {
            words = repository!!.getRandomWordsInCategories(
                selectedCategories.filter { it.value }.keys.toList(),
                wordsCount,
                Locale.getDefault().language
            ).toMutableList()
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

    private fun initRound() {
        currentRoundFinished = false
        wordsToPlay.clear()
        wordsToPlay.addAll(words.shuffled())
        wordsMissedInRound.clear()
        Log.d(
            "GameManager",
            "Starting round $currentRound - Words to Play (${wordsToPlay.size}): $wordsToPlay"
        )
    }

    fun initTurn() {
        wordsPlayedInTurn.clear()
        currentWord = wordsToPlay.firstOrNull()
        timerCurrentTime = timerTotalTime
    }

    fun playWord(found: Boolean, timerEnded: Boolean) {
        if (hasMoreWords) {
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

    fun finishRound() {
        currentRound++
        initRound()
    }

    // endregion Game Lifecycle

    // region Getters/Setters

    fun changeValueInPlayedWords(word: Pair<Word, Boolean>) {
        wordsPlayedInTurn.indexOf(word)
        wordsPlayedInTurn[wordsPlayedInTurn.indexOf(word)] =
            Pair(first = word.first, second = !word.second)
    }

    val hasMoreWords
        get() = wordsToPlay.size > 0

    val hasMoreRounds
        get() = currentRound < 2

    val wordsFoundInTurnCount
        get() = wordsPlayedInTurn.count { it.second }
    val wordsMissedInTurnCount
        get() = wordsPlayedInTurn.count { !it.second }

    fun getTeamCurrentRoundScore(team: Int) = getTeamRoundScore(team = team, round = currentRound)

    fun getTeamRoundScore(team: Int, round: Int) =
        if (currentRound < round) -1 else teamWords[team][round].size

    fun getTeamTotalScore(team: Int) = teamWords[team].sumOf { it.size }

    val shouldInvertColors
        get() = currentTeam == 0

    fun getTeamColor(team: Int) = when (team) {
        0 -> Accent
        1 -> Primary
        else -> White
    }

    val currentTeamColor
        get() = if (!currentRoundFinished) getTeamColor(currentTeam) else White

    val teamNameId
        get() = if (currentTeam == 0) R.string.team_blue else R.string.team_orange


    // endregion Getters/Setters
}
