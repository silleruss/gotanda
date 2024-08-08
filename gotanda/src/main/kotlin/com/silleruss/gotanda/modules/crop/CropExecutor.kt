package com.silleruss.gotanda.modules.crop

import arrow.core.Either
import arrow.core.raise.either
import com.silleruss.gotanda.modules.crop.services.FfmpegService
import org.springframework.stereotype.Component

@Component
class CropExecutor(
    private val ffmpegService: FfmpegService,
) {

    suspend fun execute(payload: CropVideoPayload): Either<Throwable, Unit> {
        return either { ffmpegService.execute(payload).await() }
    }

}