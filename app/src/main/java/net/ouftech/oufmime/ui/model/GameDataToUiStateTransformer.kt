package net.ouftech.oufmime.ui.model

import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.views.screens.play.PlayScreenUiState
import net.ouftech.oufmime.ui.views.screens.scoreboard.ScoreboardScreenUiState
import net.ouftech.oufmime.ui.views.screens.scoreboard.TeamScoreboardUiState
import net.ouftech.oufmime.ui.views.screens.settings.SettingsScreenUiState
import net.ouftech.oufmime.ui.views.screens.turnend.TurnEndUiState
import net.ouftech.oufmime.ui.views.screens.turnend.WordListItem
import net.ouftech.oufmime.ui.views.screens.turnstart.TurnStartUiState

interface GameDataToUiStateTransformer {
    fun getTurnStartUiState(gameData: GameData): TurnStartUiState
    fun getPlayScreenUiState(gameData: GameData): PlayScreenUiState
    fun getTurnEndUiState(gameData: GameData): TurnEndUiState
    fun getScoreboardScreenUiState(gameData: GameData): ScoreboardScreenUiState
    fun getSettingsScreenUiState(gameData: GameData): SettingsScreenUiState
}

class GameDataToUiStateTransformerImpl : GameDataToUiStateTransformer {

    override fun getTurnStartUiState(gameData: GameData) = TurnStartUiState(
        currentTeam = gameData.currentTeam,
        currentRound = gameData.currentRound,
        teamNameId = if (gameData.currentTeam == 0) R.string.team_orange else R.string.team_blue,
        blueTotalScore = getTeamTotalScore(gameData, 0),
        blueCurrentRoundScore = getTeamCurrentRoundScore(gameData, 0),
        orangeTotalScore = getTeamTotalScore(gameData, 1),
        orangeCurrentRoundScore = getTeamCurrentRoundScore(gameData, 1),
    )

    override fun getPlayScreenUiState(gameData: GameData) = PlayScreenUiState(
        wordsToPlayCount = gameData.wordsToPlay.size,
        timerMaxValue = gameData.timerTotalTime,
        currentWord = gameData.currentWord,
        invertColors = gameData.currentTeam == 1
    )

    override fun getTurnEndUiState(gameData: GameData) = TurnEndUiState(
        currentTeam = gameData.currentTeam,
        wordsPlayed = gameData.wordsPlayedInTurn.map { WordListItem.WordItem(it.first, it.second) }

    )

    override fun getScoreboardScreenUiState(gameData: GameData) = ScoreboardScreenUiState(
        hasMoreRounds = gameData.hasMoreRounds,
        teamBlueScoreboardUiState = getTeamScoreboardUiState(gameData, 0),
        teamOrangeScoreboardUiState = getTeamScoreboardUiState(gameData, 1),
    )

    override fun getSettingsScreenUiState(gameData: GameData) = SettingsScreenUiState(
        selectedCategories = gameData.selectedCategories,
        wordsCount = gameData.wordsCount,
        timerTotalTime = gameData.timerTotalTime / 1000,
    )

    private fun getTeamCurrentRoundScore(gameData: GameData, team: Int) = getTeamRoundScore(gameData, team, gameData.currentRound)

    private fun getTeamRoundScore(gameData: GameData, team: Int, round: Int) =
        if (gameData.currentRound < round) -1 else gameData.teamWords[team][round].size

    private fun getTeamTotalScore(gameData: GameData, team: Int) = gameData.teamWords[team].sumOf { it.size }

    private fun getTeamScoreboardUiState(gameData: GameData, team: Int) = TeamScoreboardUiState(
        describeScore = getTeamRoundScore(gameData, team, 0),
        wordScore = getTeamRoundScore(gameData, team, 1),
        mimeScore = getTeamRoundScore(gameData, team, 2),
        totalScore = getTeamTotalScore(gameData, team),
    )
}