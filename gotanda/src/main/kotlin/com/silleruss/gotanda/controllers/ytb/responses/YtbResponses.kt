package com.silleruss.gotanda.controllers.ytb.responses

import arrow.core.Either
import com.github.kiulian.downloader.model.Extension
import com.github.kiulian.downloader.model.videos.formats.Format
import com.github.kiulian.downloader.model.videos.formats.Itag
import com.silleruss.gotanda.core.toOptional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

data class VideoDetailResponses(
    val title: String,
    val author: String,
    val isLive: Boolean,
    val keywords: List<String>,
    val shortDescription: String,
    val averageRating: Int,
    val viewCount: Long,
    val isLiveContent: Boolean,
    val liveUrl: Optional<String>,
)

data class VideoFormatResponses(
    val iTag: Itag,
    val url: String,
    val mimeType: String,
    val extension: Extension,
    val bitrate: Int,
    val contentLength: Optional<Long>,
    val lastModified: Long,
    val approxDurationMs: Long,
    val clientVersion: String,
) {

    companion object {
        fun from(format: Format) = format.run {
            VideoFormatResponses(
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
        }
    }

}

data class CropVideoResponse(
    val id: String,
    val title: String,
)

typealias GetYtbVideoDetailResponse = Mono<VideoDetailResponses>
typealias GetYtbVideoFormatsResponse = Flux<VideoFormatResponses>

typealias CropYtbVideoResponse = Either<Throwable, CropVideoResponse>