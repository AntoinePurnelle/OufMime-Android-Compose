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

import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.data.WordsRepository
import org.junit.Before
import org.junit.Test
import java.util.Locale

class GetRandomWordsInCategoriesUseCaseTest {

    @MockK private lateinit var repository: WordsRepository

    private lateinit var useCase: GetRandomWordsInCategoriesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetRandomWordsInCategoriesUseCaseImpl(repository)
    }

    @Test
    fun invoke() = runBlocking {
        // GIVEN
        val categories = mapOf(
            "Actions" to true,
            "Geek" to false,
            "Events" to true,
        )
        val filteredCategories = listOf(
            "Actions",
            "Events",
        )
        val count = 2
        val language = "en"
        val words = listOf(
            Word("word1", Categories.ACTIONS, language),
            Word("word2", Categories.EVENTS, language),
        )

        Locale.setDefault(Locale(language))
        coEvery { repository.getRandomWordsInCategories(filteredCategories, count, language) } returns words

        // WHEN
        val result = useCase(categories, count)

        // THEN
        coVerify {
            repository.getRandomWordsInCategories(filteredCategories, count, language)
        }
        Truth.assertThat(result.words).isEqualTo(words)
    }

}