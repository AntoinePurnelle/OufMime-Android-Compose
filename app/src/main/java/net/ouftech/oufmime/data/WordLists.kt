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
    val version: Int,
    val en: TranslatedWords,
    val fr: TranslatedWords,
)

class TranslatedWords(
    val actions: List<String>,
    val activities: List<String>,
    val anatomy: List<String>,
    val animals: List<String>,
    val celebrities: List<String>,
    val clothes: List<String>,
    val events: List<String>,
    val fictional: List<String>,
    val food: List<String>,
    val geek: List<String>,
    val locations: List<String>,
    val jobs: List<String>,
    val mythology: List<String>,
    val nature: List<String>,
    val objects: List<String>,
    val vehicles: List<String>,
)