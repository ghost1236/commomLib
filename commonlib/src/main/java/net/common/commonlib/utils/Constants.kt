package net.common.commonlib.utils

object Constants {

    // Animation
    val ANIM_FRAGMENT_UP = 0
    val ANIM_FRAGMENT_DOWN = 1
    val ANIM_FRAGMENT_IN_LEFT = 2
    val ANIM_FRAGMENT_OUT_RIGHT = 3
    val ANIM_NOT = 4

    /**
     * 접속 URL
     */
    val MAIN_URL : String
        get() = "https://demo.newfrom.net:53443/app"

    /**
     * API URL
     */
    val API_URL : String
        get() = "https://demo.newfrom.net:53443/api/v1/"

    /**
     * 디바이스 등록 URL
     */
    val ADD_DEVICE_URL : String
        get() = "$MAIN_URL/device/addInfo"

    /**
     * 손 위생 모니터 관리 홈
     */
    val MONITORING_URL : String
        get() = "$MAIN_URL/monitoring"

    /**
     * 시리얼 번호 검색 URL
     */
    val SERIAL_SEARCH_URL : String
        get() = "$MAIN_URL/device/serialSearch"

    val encKey : String
        get() = "dJoviA4NUgrSM73R5uIrNaJtPCd1zA78"

    val USER_ID : String
        get() = "userId"
}