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

package net.ouftech.oufmime.data.usecases

import android.content.Context
import android.content.res.AssetManager
import com.google.common.truth.Truth
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.DataStoreManager
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.data.WordsRepository
import net.ouftech.oufmime.data.usecases.InsertWordsUseCaseImpl.Companion.FILE_NAME
import net.ouftech.oufmime.utils.Logger
import org.junit.Before
import org.junit.Test
import java.io.InputStream

class InsertWordsUseCaseTest {

    @MockK private lateinit var repository: WordsRepository

    @MockK private lateinit var dataStoreManager: DataStoreManager

    @MockK private lateinit var logger: Logger

    @MockK private lateinit var context: Context

    @MockK private lateinit var assets: AssetManager

    private lateinit var useCase: InsertWordsUseCase

    @Before
    fun setup() {
        repository = mockk()
        dataStoreManager = mockk()
        logger = mockk()
        context = mockk()
        assets = mockk()
        useCase = InsertWordsUseCaseImpl(repository, dataStoreManager, logger)
    }

    @Test
    fun `invoke - Same version`() = runBlocking {
        // GIVEN
        val inputStream: InputStream = json.byteInputStream()
        every { context.assets } returns assets
        every { assets.open(FILE_NAME) } returns inputStream
        every { dataStoreManager.getWordsListVersion() } returns flowOf(2)
        coEvery { repository.getAllWords() } returns listOf()
        every { logger.d(any()) } just Runs

        // WHEN
        val result = useCase(context)

        // THEN
        coVerify {
            context.assets
            assets.open(FILE_NAME)
            dataStoreManager.getWordsListVersion()
            repository.getAllWords()
            logger.d(any())
        }
        Truth.assertThat(result).isEqualTo(InsertWordsUseCase.Result.Success)
    }

    @Test
    fun `invoke - Newer version`() = runBlocking {
        // GIVEN
        val inputStream: InputStream = json.byteInputStream()
        every { context.assets } returns assets
        every { assets.open(FILE_NAME) } returns inputStream
        every { dataStoreManager.getWordsListVersion() } returns flowOf(1)
        coEvery { repository.insertWords(enActionWords.map { Word(it, Categories.ACTIONS, "en") }) } just Runs
        coEvery { repository.insertWords(frActionWords.map { Word(it, Categories.ACTIONS, "fr") }) } just Runs
        coEvery { repository.insertWords(listOf()) } just Runs
        coEvery { dataStoreManager.saveWordsListsVersion(2) } just Runs
        coEvery { repository.getAllWords() } returns listOf()
        every { logger.d(any()) } just Runs

        // WHEN
        val result = useCase(context)

        // THEN
        coVerify {
            context.assets
            assets.open(FILE_NAME)
            dataStoreManager.getWordsListVersion()
            repository.insertWords(enActionWords.map { Word(it, Categories.ACTIONS, "en") })
            repository.insertWords(frActionWords.map { Word(it, Categories.ACTIONS, "fr") })
            repository.insertWords(listOf())
            dataStoreManager.saveWordsListsVersion(2)
            repository.getAllWords()
            logger.d(any())
        }
        Truth.assertThat(result).isEqualTo(InsertWordsUseCase.Result.Success)
    }

    private val enActionWords = listOf("Giving birth", "Watering plants", "Start a fire")
    private val frActionWords = listOf("Accoucher", "Allumer un feu", "Arroser")

    private val json = "{\n" +
            "  \"version\": 2,\n" +
            "  \"en\": {\n" +
            "    \"actions\": [\n" +
            "      \"Giving birth\",\n" +
            "      \"Watering plants\",\n" +
            "      \"Start a fire\"\n" +
            "    ]\n" +
            "  },\n" +
            "  \"fr\": {\n" +
            "    \"actions\": [\n" +
            "      \"Accoucher\",\n" +
            "      \"Allumer un feu\",\n" +
            "      \"Arroser\"\n" +
            "    ]\n" +
            "  }\n" +
            "}"

}