package net.ouftech.oufmime.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    companion object {
        val WORDS_LIST_VERSION = intPreferencesKey("words_list_version")
    }

    suspend fun saveToDataStore(wordsListVersion: Int) {
        context.dataStore.edit {
            it[WORDS_LIST_VERSION] = wordsListVersion
        }
    }

    fun getWordsListVersionFromDataStore() =
        context.dataStore.data.map { it[WORDS_LIST_VERSION] ?: 0 }

}