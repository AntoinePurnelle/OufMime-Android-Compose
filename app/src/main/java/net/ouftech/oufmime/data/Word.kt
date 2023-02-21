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

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.ouftech.oufmime.R

@Entity
data class Word(
    @PrimaryKey
    var word: String,
    var category: Categories,
    var language: String
)

enum class Categories(@StringRes val resId: Int) {
    ACTIONS(R.string.category_actions),
    ACTIVITIES(R.string.category_activities),
    ANATOMY(R.string.category_anatomy),
    ANIMALS(R.string.category_animals),
    CELEBRITIES(R.string.category_celebrities),
    CLOTHES(R.string.category_clothes),
    EVENTS(R.string.category_events),
    FICTIONAL(R.string.category_fictional),
    FOOD(R.string.category_food),
    GEEK(R.string.category_geek),
    JOBS(R.string.category_jobs),
    LOCATIONS(R.string.category_locations),
    MYTHOLOGY(R.string.category_mythology),
    NATURE(R.string.category_nature),
    OBJECTS(R.string.category_objects),
    VEHICLES(R.string.category_vehicles)
}