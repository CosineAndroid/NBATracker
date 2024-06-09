package kr.cosine.nbatracker.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.cosine.nbatracker.data.Draft
import kr.cosine.nbatracker.data.PlayerInfo
import kr.cosine.nbatracker.data.PlayerStat
import kr.cosine.nbatracker.data.Record
import kr.cosine.nbatracker.data.TeamInfo
import kr.cosine.nbatracker.data.TeamStat
import kr.cosine.nbatracker.enums.Position
import kr.cosine.nbatracker.enums.Team
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.jsoup.Jsoup
import kotlin.math.roundToInt

object TrackerService {

    private const val ZERO = 0.0

    private const val PLAYER_URL = "https://www.nba.com/players"
    private const val TEAM_URL = "https://sports.daum.net/prx/hermes/api/team/rank.json?leagueCode=nba"

    suspend fun getPlayerInfoMap(): Map<Long, PlayerInfo> {
        return withContext(Dispatchers.IO) {
            val jsoup = Jsoup.connect(PLAYER_URL).timeout(5000).get()
            val document = jsoup.getElementById("__NEXT_DATA__")?.firstChild().toString()
            val jsonParser = JSONParser()
            val json = jsonParser.parse(document) as JSONObject
            val props = json["props"] as JSONObject
            val pageProps = props["pageProps"] as JSONObject
            val players = pageProps["players"] as JSONArray

            val playerInfos = players.mapNotNull {
                val playerJson = it as JSONObject
                val playerId = playerJson["PERSON_ID"] as Long
                val firstName = playerJson["PLAYER_FIRST_NAME"] as String
                val lastName = playerJson["PLAYER_LAST_NAME"] as String
                val jerseyNumber = (playerJson["JERSEY_NUMBER"] as? String)?.toIntOrNull()
                    ?: return@mapNotNull null
                val positionText = playerJson["POSITION"] as String
                val position = Position.findPosition(positionText) ?: return@mapNotNull null
                val teamAbbreviation =
                    playerJson["TEAM_ABBREVIATION"] as? String ?: return@mapNotNull null
                val team = Team.findTeamByShortName(teamAbbreviation) ?: return@mapNotNull null
                val height = playerJson["HEIGHT"] as String
                val weight = playerJson["WEIGHT"] as String
                val draftYear = playerJson["DRAFT_YEAR"] as? Long
                val draftRound = playerJson["DRAFT_ROUND"] as? Long
                val draftNumber = playerJson["DRAFT_NUMBER"] as? Long
                val draft = Draft(draftYear, draftRound, draftNumber)
                val points = playerJson["PTS"].toDoubleOrZero()
                val rebound = playerJson["REB"].toDoubleOrZero()
                val assist = playerJson["AST"].toDoubleOrZero()
                val playerStat = PlayerStat(points, rebound, assist)
                val country = playerJson["COUNTRY"] as String
                val college = playerJson["COLLEGE"] as String
                PlayerInfo(
                    playerId,
                    firstName,
                    lastName,
                    jerseyNumber,
                    position,
                    team,
                    height,
                    weight,
                    draft,
                    playerStat,
                    country,
                    college
                )
            }.sortedBy { it.fullName }
            return@withContext playerInfos.associateBy { it.id }
        }
    }

    fun getTeamInfoMap(): Map<Team, TeamInfo> {
        val jsoup = Jsoup.connect(TEAM_URL).timeout(5000).ignoreContentType(true).execute().body()
        val jsonParser = JSONParser()
        val json = jsonParser.parse(jsoup.toString()) as JSONObject
        val teams = json["list"] as JSONArray
        return teams.mapNotNull {
            val teamJson = it as JSONObject
            val name = teamJson["name"].toString()
            val team = Team.findTeamByKoreanName(name) ?: return@mapNotNull null

            val rankJson = teamJson["rank"] as JSONObject
            val homeRecord = rankJson.getRecord("home")
            val awayRecord = rankJson.getRecord("away")
            val streak = rankJson["streak"].toString()

            val statJson = teamJson["stat"] as JSONObject
            val points = statJson["ptsAvg"].toDoubleOrZero().round()
            val rebound = statJson["rebAvg"].toDoubleOrZero().round()
            val assist = statJson["astAvg"].toDoubleOrZero().round()
            val againstPoints = statJson["lostPtsAvg"].toDoubleOrZero().round()
            val teamStat = TeamStat(points, rebound, assist, againstPoints)

            team to TeamInfo(team, homeRecord, awayRecord, streak, teamStat)
        }.toMap()
    }

    private fun Any?.toDoubleOrZero(): Double = toString().toDoubleOrNull() ?: ZERO

    private fun Double.round(): Double {
        return (this * 10.0).roundToInt() / 10.0
    }

    private fun JSONObject.getRecord(type: String): Record {
        val win = this["${type}Win"] as Long
        val lose = this["${type}Loss"] as Long
        return Record(win, lose)
    }
}