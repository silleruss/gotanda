package com.silleruss.gotanda.modules.ytb

import org.springframework.stereotype.Component
import java.util.regex.Matcher
import java.util.regex.Pattern

@Component
class YtbHelper {

    fun generateValidMatcher(url: String): Matcher {
        return Pattern.compile(YOUTUBE_URL_REGEX).matcher(url)
    }

    companion object {

        private const val YOUTUBE_URL_REGEX = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

    }

}