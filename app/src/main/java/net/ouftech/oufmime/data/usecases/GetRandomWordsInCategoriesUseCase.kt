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

import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.data.WordsRepository
import java.util.Locale

interface GetRandomWordsInCategoriesUseCase {

    suspend operator fun invoke(categories: Map<String, Boolean>, count: Int): Result

    data class Result(val words: List<Word>)

}

class GetRandomWordsInCategoriesUseCaseImpl(
    private val repository: WordsRepository,
) : GetRandomWordsInCategoriesUseCase {

    override suspend operator fun invoke(categories: Map<String, Boolean>, count: Int) =
        GetRandomWordsInCategoriesUseCase.Result(
            repository.getRandomWordsInCategories(
                categories.filter { it.value }.keys.toList(),
                count,
                Locale.getDefault().language
            )
        )

}