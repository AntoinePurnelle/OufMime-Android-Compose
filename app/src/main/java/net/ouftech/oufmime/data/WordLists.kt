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

class WordLists(
    val version: Int = 0,
    val en: TranslatedWords = TranslatedWords(),
    val fr: TranslatedWords = TranslatedWords(),
)

class TranslatedWords(
    val actions: List<String> = listOf(),
    val activities: List<String> = listOf(),
    val anatomy: List<String> = listOf(),
    val animals: List<String> = listOf(),
    val celebrities: List<String> = listOf(),
    val clothes: List<String> = listOf(),
    val events: List<String> = listOf(),
    val fictional: List<String> = listOf(),
    val food: List<String> = listOf(),
    val geek: List<String> = listOf(),
    val locations: List<String> = listOf(),
    val jobs: List<String> = listOf(),
    val mythology: List<String> = listOf(),
    val nature: List<String> = listOf(),
    val objects: List<String> = listOf(),
    val vehicles: List<String> = listOf(),
)