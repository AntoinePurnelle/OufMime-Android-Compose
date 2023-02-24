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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<Word>)

    @Query(
        """
        SELECT * FROM Word 
        WHERE word = :word 
        AND language = :language
        LIMIT 1
        """
    )
    suspend fun getWord(word: String, language: String): Word

    @Query("SELECT * FROM Word")
    suspend fun getAllWords(): List<Word>

    @Query(
        """
        SELECT * FROM Word
        WHERE category IN (:categories)
        AND language = :language
        ORDER BY RANDOM() LIMIT :count
    """
    )
    suspend fun getRandomWordsInCategories(
        categories: List<String>,
        count: Int,
        language: String
    ): List<Word>
}