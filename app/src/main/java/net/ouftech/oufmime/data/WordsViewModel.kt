package net.ouftech.oufmime.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import net.ouftech.oufmime.data.Categories.*
import java.io.IOException

class WordsViewModel : ViewModel() {

    private var repository: WordsRepository? = null
    var currentTeam by mutableStateOf(1)
    var currentRound by mutableStateOf(1)
    var team1TotalScore by mutableStateOf(0)
    var team1CurrentRoundScore by mutableStateOf(0)
    var team2TotalScore by mutableStateOf(0)
    var team2CurrentRoundScore by mutableStateOf(0)


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
}