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

class WordsRepository(private val repository: WordsDao) {
    suspend fun insertWord(word: Word) = repository.insertWord(word)
    suspend fun insertWords(words: List<Word>) = repository.insertWords(words)
    suspend fun getWord(word: String, language: String) = repository.getWord(word, language)
    suspend fun getAllWords() = repository.getAllWords()
    suspend fun getRandomWordsInCategories(categories: List<String>, count: Int, language: String) =
        repository.getRandomWordsInCategories(categories, count, language)
}