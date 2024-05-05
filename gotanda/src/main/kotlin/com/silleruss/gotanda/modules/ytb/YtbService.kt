package com.silleruss.gotanda.modules.ytb

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo
import com.github.kiulian.downloader.model.videos.VideoDetails
import com.github.kiulian.downloader.model.videos.VideoInfo
import com.github.kiulian.downloader.model.videos.formats.Format
import com.silleruss.gotanda.controllers.ytb.requests.GetYtbVideoDetailRequest
import com.silleruss.gotanda.controllers.ytb.responses.GetYtbVideoDetailResponse
import com.silleruss.gotanda.controllers.ytb.responses.VideoDetailResponses
import com.silleruss.gotanda.controllers.ytb.responses.VideoFormatResponses
import com.silleruss.gotanda.core.toMono
import com.silleruss.gotanda.core.toOptional
import com.silleruss.gotanda.exceptions.ytb.CannotExtractYtbUrlException
import com.silleruss.gotanda.modules.crop.CropExecutor
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class YtbService(
    private val cropExecutor: CropExecutor,
) {

    private val downloader = YoutubeDownloader()

    fun getVideoDetail(request: GetYtbVideoDetailRequest): GetYtbVideoDetailResponse {
        val videoInfo = extractVideoDataFromUrl(request.url)

        return videoInfo
            .map { it.details() }
            .map { it.create() }
            .also { println(it.toString()) }
            .toMono()
    }

    fun getVideoFormat() {
        TODO("implement get ytb vidio formats information")
    }

    fun cropVideo() {
        TODO("""
            crop ytb video with cropExecutor.execute()
        """.trimIndent())
    }

    private fun extractVideoDataFromUrl(url: String): Either<Throwable, VideoInfo> {
        return either {
            val ytbMatcher = Pattern.compile(YOUTUBE_URL_REGEX).matcher(url)

            ensure(ytbMatcher.find()) {
                CannotExtractYtbUrlException(url)
            }

            val videoId = ytbMatcher.group()
            val ytbRequest = RequestVideoInfo(videoId)
            val videoInfo = downloader.getVideoInfo(ytbRequest)

            videoInfo.data()
        }
    }

    private fun VideoDetails.create() = VideoDetailResponses(
        title = title(),
        author = author(),
        isLive = isLive,
        keywords = keywords(),
        shortDescription = description(),
        averageRating = averageRating(),
        viewCount = viewCount(),
        isLiveContent,
        liveUrl = liveUrl().toOptional(),
    )

    private fun Format.create() = VideoFormatResponses(
        iTag = itag(),
        url = url(),
        mimeType = mimeType(),
        extension = extension(),
        bitrate = bitrate(),
        contentLength = contentLength().toOptional(),
        lastModified = lastModified(),
        approxDurationMs = duration(),
        clientVersion = clientVersion(),
    )

    companion object {

        private const val YOUTUBE_URL_REGEX = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

    }

}