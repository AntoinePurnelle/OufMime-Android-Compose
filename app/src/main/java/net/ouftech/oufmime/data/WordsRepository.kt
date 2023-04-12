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

package net.ouftech.oufmime.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface WordsRepository {
    suspend fun insertWord(word: Word)
    suspend fun insertWords(words: List<Word>)
    suspend fun getWord(word: String, language: String): Word
    suspend fun getAllWords(): List<Word>
    suspend fun getRandomWordsInCategories(
        categories: List<String>,
        count: Int,
        language: String
    ): List<Word>
}

class WordsRepositoryImpl(private val dao: WordsDao) : WordsRepository {

    override suspend fun insertWord(word: Word) = withContext(Dispatchers.IO) { dao.insertWord(word) }

    override suspend fun insertWords(words: List<Word>) = withContext(Dispatchers.IO) { dao.insertWords(words) }

    override suspend fun getWord(
        word: String,
        language: String
    ) = withContext(Dispatchers.IO) { dao.getWord(word, language) }

    override suspend fun getAllWords() = withContext(Dispatchers.IO) { dao.getAllWords() }

    override suspend fun getRandomWordsInCategories(
        categories: List<String>,
        count: Int,
        language: String
    ) = withContext(Dispatchers.IO) { dao.getRandomWordsInCategories(categories, count, language) }

}