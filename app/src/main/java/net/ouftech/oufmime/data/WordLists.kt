package net.ouftech.oufmime.data

class WordLists(
    val version: Int,
    val en: TranslatedWords,
    val fr: TranslatedWords,
)

class TranslatedWords(
    val actions: List<String>,
    val activities: List<String>,
    val anatomy: List<String>,
    val animals: List<String>,
    val celebrities: List<String>,
    val clothes: List<String>,
    val events: List<String>,
    val fictional: List<String>,
    val food: List<String>,
    val geek: List<String>,
    val locations: List<String>,
    val jobs: List<String>,
    val mythology: List<String>,
    val nature: List<String>,
    val objects: List<String>,
    val vehicles: List<String>,
)