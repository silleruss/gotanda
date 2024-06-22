package com.silleruss.gotanda.modules.crop

import com.silleruss.gotanda.modules.crop.services.FfmpegService
import org.springframework.stereotype.Component

@Component
class CropExecutor(
    private val ffmpegService: FfmpegService,
) {

    fun execute(payload: CropVideoPayload) {
        ffmpegService.execute(payload)
    }

}