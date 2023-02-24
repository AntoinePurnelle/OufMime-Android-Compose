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

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface DataStoreManager {
    suspend fun saveWordsListsVersion(wordsListVersion: Int)
    fun getWordsListVersion(): Flow<Int>
}

class DataStoreManagerImpl(private val context: Context) : DataStoreManager {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override suspend fun saveWordsListsVersion(wordsListVersion: Int) {
        context.dataStore.edit {
            it[WORDS_LIST_VERSION] = wordsListVersion
        }
    }

    override fun getWordsListVersion() = context.dataStore.data.map { it[WORDS_LIST_VERSION] ?: 0 }

    companion object {
        val WORDS_LIST_VERSION = intPreferencesKey("words_list_version")
    }

}