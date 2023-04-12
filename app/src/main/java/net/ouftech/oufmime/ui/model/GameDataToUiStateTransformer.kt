package net.ouftech.oufmime.ui.model

import androidx.compose.ui.graphics.Color
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.BlueTeam
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.OrangeTeam
import net.ouftech.oufmime.ui.views.screens.PlayScreenUiState
import net.ouftech.oufmime.ui.views.screens.ScoreboardScreenUiState
import net.ouftech.oufmime.ui.views.screens.SettingsScreenUiState
import net.ouftech.oufmime.ui.views.screens.TeamScoreboardUiState
import net.ouftech.oufmime.ui.views.screens.TurnEndUiState
import net.ouftech.oufmime.ui.views.screens.TurnStartUiState

interface GameDataToUiStateTransformer {
    fun getTurnStartUiState(gameData: GameData, dimens: Dimens): TurnStartUiState
    fun getPlayScreenUiState(gameData: GameData, dimens: Dimens): PlayScreenUiState
    fun getTurnEndUiState(gameData: GameData, dimens: Dimens): TurnEndUiState
    fun getScoreboardScreenUiState(gameData: GameData, dimens: Dimens): ScoreboardScreenUiState
    fun getSettingsScreenUiState(gameData: GameData, dimens: Dimens): SettingsScreenUiState
}

class GameDataToUiStateTransformerImpl : GameDataToUiStateTransformer {

    override fun getTurnStartUiState(gameData: GameData, dimens: Dimens) = TurnStartUiState(
        dimens = dimens,
        currentTeam = gameData.currentTeam,
        currentRound = gameData.currentRound,
        teamNameId = if (gameData.currentTeam == 0) R.string.team_orange else R.string.team_blue,
        blueTotalScore = getTeamTotalScore(gameData, 0),
        blueCurrentRoundScore = getTeamCurrentRoundScore(gameData, 0),
        orangeTotalScore = getTeamTotalScore(gameData, 1),
        orangeCurrentRoundScore = getTeamCurrentRoundScore(gameData, 1),
    )

    override fun getPlayScreenUiState(gameData: GameData, dimens: Dimens) = PlayScreenUiState(
        dimens = dimens,
        wordsToPlayCount = gameData.wordsToPlay.size,
        timerMaxValue = gameData.timerTotalTime,
        currentWord = gameData.currentWord,
        invertColors = gameData.currentTeam == 1
    )

    override fun getTurnEndUiState(gameData: GameData, dimens: Dimens) = TurnEndUiState(
        dimens = dimens,
        currentTeam = gameData.currentTeam,
        wordsPlayed = gameData.wordsPlayedInTurn
    )

    override fun getScoreboardScreenUiState(gameData: GameData, dimens: Dimens) = ScoreboardScreenUiState(
        dimens = dimens,
        hasMoreRounds = gameData.hasMoreRounds,
        teamBlueScoreboardUiState = getTeamScoreboardUiState(gameData, 0),
        teamOrangeScoreboardUiState = getTeamScoreboardUiState(gameData, 1),
    )

    override fun getSettingsScreenUiState(gameData: GameData, dimens: Dimens) = SettingsScreenUiState(
        dimens = dimens,
        selectedCategories = gameData.selectedCategories,
        wordsCount = gameData.wordsCount,
        timerTotalTime = gameData.timerTotalTime / 1000,
    )

    private fun getTeamCurrentRoundScore(gameData: GameData, team: Int) = getTeamRoundScore(gameData, team, gameData.currentRound)

    private fun getTeamRoundScore(gameData: GameData, team: Int, round: Int) =
        if (gameData.currentRound < round) -1 else gameData.teamWords[team][round].size

    private fun getTeamTotalScore(gameData: GameData, team: Int) = gameData.teamWords[team].sumOf { it.size }

    private fun getTeamScoreboardUiState(gameData: GameData, team: Int) = TeamScoreboardUiState(
        color = getTeamColor(team),
        describeScore = getTeamRoundScore(gameData, team, 0),
        wordScore = getTeamRoundScore(gameData, team, 1),
        mimeScore = getTeamRoundScore(gameData, team, 2),
        totalScore = getTeamTotalScore(gameData, team),
    )

    private fun getTeamColor(team: Int) = when (team) {
        0 -> BlueTeam
        1 -> OrangeTeam
        else -> Color.White
    }
}