# OufMime-Android-Compose
What started in 2020 as a support for a family game, became my playground to experiment with new techs.

## Tech, Tools and Frameworks:
* Kotlin, Kotlin DSL
* Jetpack Compose
* MVVM
* Koin
* MockK
* DataStore
* Room DB
* Custom ScreenConfiguration Locale with sized dimens to better handle screen sizes
* Custom Preview Interface to auto generate previews in different configurations

## Game description
OufMime is a family game where two teams will try to guess the most words.
It is played in three rounds:
1. The player can describe the word to his team. He can't translate it, use another word of the same
   etymology or mime it. Teammates have as many guess as they want
2. Reusing the same set of words as in the first round, the payer can only une one word to describe
   the one to guess. Teammates have only one guess per word
3. Still using the same set, the player has to mime the word. He can make sounds but cannot pronounce a word. Teammates have as many guess as they want

Available in English and French (UI + Words)
