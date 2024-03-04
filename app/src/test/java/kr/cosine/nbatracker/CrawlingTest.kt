package kr.cosine.nbatracker

import kr.cosine.nbatracker.enums.Team
import kr.cosine.nbatracker.service.TrackerService
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.jsoup.Jsoup
import org.junit.Test

class CrawlingTest {

    @Test
    fun player_info_test() {
        showTeamid()
    }

    fun showTeamid() {
        val jsoup = Jsoup.connect("https://www.nba.com/players").post()
        val document = jsoup.getElementById("__NEXT_DATA__")?.firstChild().toString()
        val jsonParser = JSONParser()
        val json = jsonParser.parse(document) as JSONObject
        val props = json["props"] as JSONObject
        val pageProps = props["pageProps"] as JSONObject
        val players = pageProps["players"] as JSONArray
        val teamMap = mutableMapOf<Team, Long>()
        players.forEach {
            val player = it as JSONObject
            val teamAbbreviation = player["TEAM_ABBREVIATION"] as? String ?: return@forEach
            val team = Team.findTeamByShortName(teamAbbreviation) ?: return@forEach
            teamMap[team] = player["TEAM_ID"] as Long
        }
        teamMap.forEach { (t, u) ->
            println("$u    :     $t")
        }
    }

    @Test
    fun team_info_test() {
        val teamInfoMap = TrackerService.getTeamInfoMap()
        Team.values.forEach {
            if (!teamInfoMap.keys.contains(it)) {
                println("${it.koreanName} 없음")
            }
        }
        teamInfoMap.forEach { (team, teamInfo) ->
            println("${team.koreanName} : $teamInfo")
        }
    }
}