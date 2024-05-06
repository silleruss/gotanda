package com.silleruss.gotanda.modules.ytb

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo
import com.github.kiulian.downloader.model.videos.VideoDetails
import com.github.kiulian.downloader.model.videos.VideoInfo
import com.github.kiulian.downloader.model.videos.formats.Format
import com.silleruss.gotanda.controllers.ytb.requests.CropYtbVideoRequest
import com.silleruss.gotanda.controllers.ytb.requests.GetYtbVideoDetailRequest
import com.silleruss.gotanda.controllers.ytb.requests.GetYtbVideoFormatRequest
import com.silleruss.gotanda.controllers.ytb.responses.*
import com.silleruss.gotanda.core.kstNow
import com.silleruss.gotanda.core.toFlux
import com.silleruss.gotanda.core.toMono
import com.silleruss.gotanda.core.toOptional
import com.silleruss.gotanda.exceptions.ytb.CannotExtractYtbUrlException
import com.silleruss.gotanda.modules.crop.CropExecutor
import com.silleruss.gotanda.modules.crop.CropVideoPayload
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class YtbService(
    private val cropExecutor: CropExecutor,
    private val resourceLoader: ResourceLoader,
) {

    private val downloader = YoutubeDownloader()

    fun getVideoDetail(request: GetYtbVideoDetailRequest): GetYtbVideoDetailResponse {
        val videoInfo = extractVideoDataFromUrl(request.url)

        return videoInfo
            .map { it.details() }
            .map { it.create() }
            .toMono()
    }

    fun getVideoFormats(request: GetYtbVideoFormatRequest): GetYtbVideoFormatsResponse {
        return getVideoFormats(request.url).toFlux()
    }

    fun cropVideo(request: CropYtbVideoRequest): CropYtbVideoResponse {
        return either {
            val videoInfo = extractVideoDataFromUrl(request.url).bind()
            // TODO: found a best format
            val format = videoInfo.videoFormats().first()

            val targetUrl = format.url()

            // TODO: modify file name patterns e.g. use video title and regex special characters
            val fileName = "${UUID.randomUUID()}"
            val outputPath = generateOutputName(fileName)

            val payload = CropVideoPayload(targetUrl, outputPath, request.startTime, request.durationTime, request.fileFormat)

            cropExecutor.execute(payload)

            // TODO: upload from

            CropVideoResponse(fileName, videoInfo.details().title())
        }.toMono()
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

    private fun getVideoFormats(url: String): Either<Throwable, List<VideoFormatResponses>> {
        return extractVideoDataFromUrl(url)
            .map { it.videoFormats() }
            .map { formats -> formats.map { it.create() } }
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

    // FIXME
    private fun generateOutputName(value: String): String {
        // build > resources > temp
        return "${resourceLoader.getResource("classpath:temp").file.path}\\$value"
    }

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