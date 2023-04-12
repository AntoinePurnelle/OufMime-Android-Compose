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

package net.ouftech.oufmime.data.usecases

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import kotlinx.coroutines.flow.catch
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.DataStoreManager
import net.ouftech.oufmime.data.TranslatedWords
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.data.WordLists
import net.ouftech.oufmime.data.WordsRepository
import net.ouftech.oufmime.utils.Logger
import java.io.IOException

interface InsertWordsUseCase {

    suspend operator fun invoke(context: Context): Result

    sealed interface Result {
        object Success : Result
        data class Error(val error: Exception) : Result
    }

}

class InsertWordsUseCaseImpl(
    private val repository: WordsRepository,
    private val dataStoreManager: DataStoreManager,
    private val logger: Logger,
) : InsertWordsUseCase {

    override suspend operator fun invoke(context: Context) = try {
        val jsonFileString = getJsonDataFromAsset(context)

        val words = Gson().fromJson(jsonFileString, WordLists::class.java)

        dataStoreManager.getWordsListVersion().catch { e -> logger.e(e) }.collect { savedVersion ->
            if (savedVersion < words.version) {
                insertLanguageWords("en", words.en)
                insertLanguageWords("fr", words.fr)

                dataStoreManager.saveWordsListsVersion(words.version)
            }

            val allWords = repository.getAllWords()

            logger.d("All Words (${allWords.size}) $allWords")
        }

        InsertWordsUseCase.Result.Success
    } catch (e: IOException) {
        InsertWordsUseCase.Result.Error(e)
    }

    private suspend fun insertLanguageWords(language: String, words: TranslatedWords) {
        insertListOfWords(Categories.ACTIONS, words.actions, language)
        insertListOfWords(Categories.ACTIVITIES, words.activities, language)
        insertListOfWords(Categories.ANATOMY, words.anatomy, language)
        insertListOfWords(Categories.ANIMALS, words.animals, language)
        insertListOfWords(Categories.CELEBRITIES, words.celebrities, language)
        insertListOfWords(Categories.CLOTHES, words.clothes, language)
        insertListOfWords(Categories.EVENTS, words.events, language)
        insertListOfWords(Categories.FICTIONAL, words.fictional, language)
        insertListOfWords(Categories.FOOD, words.food, language)
        insertListOfWords(Categories.GEEK, words.geek, language)
        insertListOfWords(Categories.LOCATIONS, words.locations, language)
        insertListOfWords(Categories.JOBS, words.jobs, language)
        insertListOfWords(Categories.MYTHOLOGY, words.mythology, language)
        insertListOfWords(Categories.NATURE, words.nature, language)
        insertListOfWords(Categories.OBJECTS, words.objects, language)
        insertListOfWords(Categories.VEHICLES, words.vehicles, language)
    }

    private suspend fun insertListOfWords(category: Categories, wordsToSort: List<String>, language: String) =
        repository.insertWords(wordsToSort.map { Word(it, category, language) })

    private fun getJsonDataFromAsset(context: Context) = context.assets.open(FILE_NAME).bufferedReader().use { it.readText() }

    companion object {
        @VisibleForTesting const val FILE_NAME = "words.json"
    }
}