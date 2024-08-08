package com.silleruss.gotanda.modules.ytb

import arrow.core.raise.either
import arrow.core.raise.ensure
import com.github.kiulian.downloader.model.videos.VideoDetails
import com.silleruss.gotanda.controllers.ytb.requests.CropYtbVideoRequest
import com.silleruss.gotanda.controllers.ytb.requests.GetYtbVideoDetailRequest
import com.silleruss.gotanda.controllers.ytb.requests.GetYtbVideoFormatRequest
import com.silleruss.gotanda.controllers.ytb.responses.*
import com.silleruss.gotanda.core.toFlux
import com.silleruss.gotanda.core.toMono
import com.silleruss.gotanda.core.toOptional
import com.silleruss.gotanda.exceptions.ffmpeg.CannotCropVideoException
import com.silleruss.gotanda.modules.crop.CropExecutor
import com.silleruss.gotanda.modules.crop.CropVideoPayload
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.util.*

@Service
class YtbService(
    private val extractor: YtbExtractor,
    private val cropExecutor: CropExecutor,
    private val resourceLoader: ResourceLoader,
) {

    fun getVideoDetail(request: GetYtbVideoDetailRequest): GetYtbVideoDetailResponse {
        val videoInfo = extractor.extractVideoDataFromUrl(request.url)

        return videoInfo
            .map { it.details() }
            .map { it.create() }
            .toMono()
    }

    fun getVideoFormats(request: GetYtbVideoFormatRequest): GetYtbVideoFormatsResponse {
        return extractor.extractVideoDataFromUrl(request.url)
            .map { it.videoFormats() }
            .map { formats -> formats.map { VideoFormatResponses.from(it) } }
            .toFlux()
    }

    suspend fun cropVideo(request: CropYtbVideoRequest): CropYtbVideoResponse {
        return either {
            val videoInfo = extractor.extractVideoDataFromUrl(request.url).bind()
            // TODO: found a best format
            val format = videoInfo.videoFormats().first()

            val targetUrl = format.url()

            // TODO: modify file name patterns e.g. use video title and regex special characters
            val fileName = "${UUID.randomUUID()}"
            val outputPath = generateOutputName(fileName)

            val payload = CropVideoPayload(targetUrl, outputPath, request.startTime, request.durationTime, request.fileFormat)

            ensure(payload.canCropVideo()) {
                CannotCropVideoException(payload)
            }

            cropExecutor.execute(payload).bind()

            // TODO: upload from

            CropVideoResponse(fileName, videoInfo.details().title())
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

    // FIXME
    private fun generateOutputName(value: String): String {
        // build > resources > temp
        return "${resourceLoader.getResource("classpath:temp").file.path}\\$value"
    }


}