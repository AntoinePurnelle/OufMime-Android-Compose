package net.ouftech.oufmime.data

class WordsRepository(private val repository: WordsDao) {

    suspend fun insertWord(word: Word) = repository.insertWord(word)
    suspend fun insertWords(words: List<Word>) = repository.insertWords(words)
    suspend fun getWord(word: String) = repository.getWord(word)
    suspend fun getAllWords() = repository.getAllWords()
    suspend fun getRandomWordsInCategories(categories: List<String>, count: Int) =
        repository.getRandomWordsInCategories(categories, count)
}