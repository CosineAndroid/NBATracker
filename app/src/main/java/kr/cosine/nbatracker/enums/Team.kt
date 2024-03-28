package kr.cosine.nbatracker.enums

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import java.io.Serializable

enum class Team(
    private val id: Long,
    val englishName: String,
    val shortName: String,
    val koreanName: String,
    val conference: Conference,
    colorCode: Long
) : Serializable {
    BOSTON_CELTICS(1610612738, "Boston Celtics", "BOS", "보스턴 셀틱스", Conference.EAST, 0xFF008348),
    BROOKLYN_NETS(1610612751, "Brooklyn Nets", "BKN", "브루클린 네츠", Conference.EAST, 0xFF2083A1),
    NEW_YORK_KNICKS(1610612752, "New York Knicks", "NYK", "뉴욕 닉스", Conference.EAST, 0xFF006bb6),
    PHILADELPHIA_76ERS(1610612755, "Philadelphia 76ers", "PHI", "필라델피아 세븐티식서스", Conference.EAST, 0xFF006bb6),
    TORONTO_RAPTORS(1610612761, "Toronto Raptors", "TOR", "토론토 랩터스", Conference.EAST, 0xFF000000),
    CHICAGO_BULLS(1610612741, "Chicago Bulls", "CHI", "시카고 불스", Conference.EAST, 0xFFCE1141),
    CLEVELAND_CAVALIERS(1610612739, "Cleveland Cavaliers", "CLE", "클리블랜드 캐벌리어스", Conference.EAST, 0xFF6F263D),
    DETROIT_PISTONS(1610612765, "Detroit Pistons", "DET", "디트로이트 피스톤즈", Conference.EAST, 0xFF1D428A),
    INDIANA_PACERS(1610612754, "Indiana Pacers", "IND", "인디애나 페이서스", Conference.EAST, 0xFF002D62),
    MILWAUKEE_BUCKS(1610612749, "Milwaukee Bucks", "MIL", "밀워키 벅스", Conference.EAST, 0xFF00471B),
    ATLANTA_HAWKS(1610612737, "Atlanta Hawks", "ATL", "애틀랜타 호크스", Conference.EAST, 0xFFE03A3E),
    CHARLOTTE_HORNETS(1610612766, "Charlotte Hornets", "CHA", "샬럿 호네츠", Conference.EAST, 0xFF00788C),
    MIAMI_HEAT(1610612748, "Miami Heat", "MIA", "마이애미 히트", Conference.EAST, 0xFF98002E),
    ORLANDO_MAGIC(1610612753, "Orlando Magic", "ORL", "올랜도 매직", Conference.EAST, 0xFF0077C0),
    WASHINGTON_WIZARDS(1610612764, "Washington Wizards", "WAS", "워싱턴 위저즈", Conference.EAST, 0xFF002B5C),
    DENVER_NUGGETS(1610612743, "Denver Nuggets", "DEN", "덴버 너기츠", Conference.WEST, 0xFF0E2240),
    MINNESOTA_TIMBERWOLVES(1610612750, "Minnesota Timberwolves", "MIN", "미네소타 팀버울브스", Conference.WEST, 0xFF0C2340),
    OKLAHOMA_CITY_THUNDER(1610612760, "Oklahoma City Thunder", "OKC", "오클라호마시티 썬더", Conference.WEST, 0xFF007AC1),
    PORTLAND_TRAIL_BLAZERS(1610612757, "Portland Trail Blazers", "POR", "포틀랜드 트레일블레이저스", Conference.WEST, 0xFFE03A3E),
    UTAH_JAZZ(1610612762, "Utah Jazz", "UTA", "유타 재즈", Conference.WEST, 0xFF7A6CCF),
    GOLDEN_STATE_WARRIORS(1610612744, "Golden State Warriors", "GSW", "골든스테이트 워리어스", Conference.WEST, 0xFF006BB6),
    LA_CLIPPERS(1610612746, "LA Clippers", "LAC", "LA 클리퍼스", Conference.WEST, 0xFFC8102E),
    LOS_ANGELES_LAKERS(1610612747, "Los Angeles Lakers", "LAL", "LA 레이커스", Conference.WEST, 0xFF552583),
    PHOENIX_SUNS(1610612756, "Phoenix Suns", "PHX", "피닉스 선즈", Conference.WEST, 0xFF1D1160),
    SACRAMENTO_KINGS(1610612758, "Sacramento Kings", "SAC", "새크라멘토 킹스", Conference.WEST, 0xFF5A2D81),
    DALLAS_MAVERICKS(1610612742, "Dallas Mavericks", "DAL", "댈러스 매버릭스", Conference.WEST, 0xFF0053BC),
    HOUSTON_ROCKETS(1610612745, "Houston Rockets", "HOU", "휴스턴 로키츠", Conference.WEST, 0xFFCE1141),
    MEMPHIS_GRIZZLIES(1610612763, "Memphis Grizzlies", "MEM", "멤피스 그리즐리스", Conference.WEST, 0xFF5D76A9),
    NEW_ORLEANS_PELICANS(1610612740, "New Orleans Pelicans", "NOP", "뉴올리언스 펠리컨스", Conference.WEST, 0xFF002B5C),
    SAN_ANTONIO_SPURS(1610612759, "San Antonio Spurs", "SAS", "샌안토니오 스퍼스", Conference.WEST, 0xFF218A73);

    val color = Color(colorCode)

    @Composable
    fun getModel(): ImageRequest {
        return ImageRequest.Builder(LocalContext.current)
            .data(TEAM_IMAGE_URL.format(id))
            .decoderFactory(SvgDecoder.Factory())
            .build()
    }

    companion object {
        private const val TEAM_IMAGE_URL = "https://cdn.nba.com/logos/nba/%d/global/L/logo.svg"

        private val values = entries.toList()

        fun findTeamByShortName(shortName: String): Team? {
            return values.find { it.shortName == shortName }
        }

        fun findTeamByKoreanName(koreanName: String): Team? {
            return values.find { it.koreanName == koreanName }
        }
    }
}