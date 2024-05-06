package com.silleruss.gotanda.controllers.ytb.responses

import com.github.kiulian.downloader.model.Extension
import com.github.kiulian.downloader.model.videos.formats.Itag
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
)

data class CropVideoResponse(
    val croppedUrl: String,
)

typealias GetYtbVideoDetailResponse = Mono<VideoDetailResponses>
typealias GetYtbVideoFormatsResponse = Flux<VideoFormatResponses>
typealias CropYtbVideoResponse = Mono<CropVideoResponse>