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

    /**
     * repository.getAllWords.observe(this, Observer {words -> ...})
     */
    @Query(
        """
        SELECT * FROM Word 
        WHERE word = :word 
        AND language = :language
        LIMIT 1
        """
    )
    suspend fun getWord(word: String, language: String): Word

    /**
     * repository.getAllWords.observe(this, Observer {words -> ...})
     */
    @Query("SELECT * FROM Word")
    suspend fun getAllWords(): List<Word>

    /**
     * repository.getRandomWords.observe(this, Observer {words -> ...})
     */
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