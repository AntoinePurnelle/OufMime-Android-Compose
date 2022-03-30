package net.ouftech.oufmime.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1)
abstract class WordsDB : RoomDatabase() {

    abstract fun wordsDao(): WordsDao

    companion object {
        @Volatile
        private var INSTANCE: WordsDB? = null

        fun getDataBase(context: Context): WordsDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    WordsDB::class.java,
                    "OufMime"
                ).build()
            }

            return INSTANCE!!
        }
    }
}