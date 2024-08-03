package com.silleruss.gotanda.modules.ytb

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo
import com.github.kiulian.downloader.model.videos.VideoInfo
import com.silleruss.gotanda.exceptions.ytb.CannotExtractYtbUrlException
import org.springframework.stereotype.Service

@Service
class YtbExtractor(
    private val helper: YtbHelper,
) {

    private val downloader = YoutubeDownloader()

    fun extractVideoDataFromUrl(url: String): Either<Throwable, VideoInfo> {
        return either {
            val matcher = helper.generateValidMatcher(url)

            ensure(matcher.find()) {
                CannotExtractYtbUrlException(url)
            }

            val videoId = matcher.group()
            val ytbRequest = RequestVideoInfo(videoId)
            val videoInfo = downloader.getVideoInfo(ytbRequest)

            videoInfo.data()
        }
    }

}