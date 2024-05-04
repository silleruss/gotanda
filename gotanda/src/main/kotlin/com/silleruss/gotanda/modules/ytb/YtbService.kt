package com.silleruss.gotanda.modules.ytb

import com.silleruss.gotanda.modules.crop.CropExecutor
import org.springframework.stereotype.Service

@Service
class YtbService(
    private val cropExecutor: CropExecutor,
) {

    fun getVideoDetail() {
        TODO("implement get ytb video detail")
    }

    fun getVideoFormat() {
        TODO("implement get ytb vidio formats information")
    }

    fun cropVideo() {
        TODO("""
            crop ytb video with cropExecutor.execute()
        """.trimIndent())
    }

}