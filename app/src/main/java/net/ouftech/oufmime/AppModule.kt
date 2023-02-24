package net.ouftech.oufmime

import android.app.Application
import androidx.room.Room
import net.ouftech.oufmime.data.*
import net.ouftech.oufmime.ui.MainActivityViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    fun provideDataStoreManager(application: Application): DataStoreManager = DataStoreManagerImpl(application)
    singleOf(::WordsRepositoryImpl) { bind<WordsRepository>() }
    factoryOf(::WordsAccessUseCaseImpl) { bind<WordsAccessUseCase>() }
    singleOf(::MainActivityViewModel)
    single { provideDataStoreManager(androidApplication()) }
}

val dbModule = module {
    fun provideDataBase(application: Application) = Room.databaseBuilder(
        context = application, klass = WordsDB::class.java, name = "OufMime"
    ).fallbackToDestructiveMigration().build()

    fun provideDao(dataBase: WordsDB) = dataBase.wordsDao
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
}