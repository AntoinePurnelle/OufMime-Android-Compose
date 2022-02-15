package net.ouftech.oufmime.data

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.ouftech.oufmime.R

@Entity
data class Word(
    @PrimaryKey
    var word: String,
    var category: Categories
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
    JOBS(R.string.category_jobs),
    LOCATIONS(R.string.category_locations),
    NATURE(R.string.category_nature),
    OBJECTS(R.string.category_objects),
    VEHICLES(R.string.category_vehicles),
}