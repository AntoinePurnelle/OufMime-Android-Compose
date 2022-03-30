package net.ouftech.oufmime.data

class WordsRepository(private val repository: WordsDao) {

    suspend fun insertWord(word: Word) = repository.insertWord(word)
    suspend fun insertWords(words: List<Word>) = repository.insertWords(words)
    suspend fun getWord(word: String, language: String) = repository.getWord(word, language)
    suspend fun getAllWords() = repository.getAllWords()
    suspend fun getRandomWordsInCategories(categories: List<String>, count: Int, language: String) =
        repository.getRandomWordsInCategories(categories, count, language)
}