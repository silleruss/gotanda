package com.silleruss.gotanda.modules.crop.services

object FfmpegHelper {

    class Container(
        val ffmpegService: FfmpegService,
    )

    fun getContainer(): Container {
        val ffmpegPath = ""
        val ffprobePath = ""

        val ffmpegService = FfmpegService(ffmpegPath, ffprobePath)

        return Container(
            ffmpegService = ffmpegService
        )
    }

}