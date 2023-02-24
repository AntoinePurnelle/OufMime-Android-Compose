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
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1)
abstract class WordsDB : RoomDatabase() {

    abstract val wordsDao: WordsDao

    companion object {
        @Volatile
        private var INSTANCE: WordsDB? = null

        @Suppress("unused")
        fun getDataBase(context: Context): WordsDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = WordsDB::class.java,
                    name = "OufMime"
                ).build()
            }

            return INSTANCE!!
        }
    }
}