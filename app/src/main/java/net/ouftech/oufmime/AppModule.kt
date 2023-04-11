package net.ouftech.oufmime

import android.app.Application
import androidx.room.Room
import net.ouftech.oufmime.data.DataStoreManager
import net.ouftech.oufmime.data.DataStoreManagerImpl
import net.ouftech.oufmime.data.WordsAccessUseCase
import net.ouftech.oufmime.data.WordsAccessUseCaseImpl
import net.ouftech.oufmime.data.WordsDB
import net.ouftech.oufmime.data.WordsRepository
import net.ouftech.oufmime.data.WordsRepositoryImpl
import net.ouftech.oufmime.ui.GameDataToUiStateTransformer
import net.ouftech.oufmime.ui.GameDataToUiStateTransformerImpl
import net.ouftech.oufmime.ui.MainActivityViewModel
import net.ouftech.oufmime.utils.Logger
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
    factoryOf(::GameDataToUiStateTransformerImpl) { bind<GameDataToUiStateTransformer>() }
    single { provideDataStoreManager(androidApplication()) }
    factoryOf(::Logger)
}

val dbModule = module {
    fun provideDataBase(application: Application) = Room.databaseBuilder(
        context = application, klass = WordsDB::class.java, name = "OufMime"
    ).fallbackToDestructiveMigration().build()

    fun provideDao(dataBase: WordsDB) = dataBase.wordsDao
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
}