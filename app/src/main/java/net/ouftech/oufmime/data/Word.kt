package net.ouftech.oufmime.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    @PrimaryKey
    var word: String,
    var category: Categories
)

enum class Categories {
    ACTIONS, ANIMALS, EVENTS, LOCATIONS, JOBS, NATURE, FOOD, OBJECTS, ANATOMY, CELEBRITIES, ACTIVITIES, VEHICLES, CLOTHES, FICTIONAL
}