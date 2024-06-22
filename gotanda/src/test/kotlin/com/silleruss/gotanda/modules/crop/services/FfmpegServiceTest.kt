package com.silleruss.gotanda.modules.crop.services

import com.silleruss.gotanda.exceptions.ffmpeg.CannotCropVideoException
import com.silleruss.gotanda.modules.crop.CropVideoPayload
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class FfmpegServiceTest {

    private val container = FfmpegHelper.getContainer()

    @Test
    fun `execute cropping empty input url`() {
        // given
        val payload = CropVideoPayload(
            "",
            "",
            1.0f,
            3.0f,
            "gif"
        )

        // when
        val result = container.ffmpegService.execute(payload)

        // then
        result.shouldBeLeft().run {
            this.shouldBeInstanceOf<CannotCropVideoException>()
        }
    }

}