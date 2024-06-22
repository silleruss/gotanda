package com.silleruss.gotanda.modules.crop.services

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.silleruss.gotanda.exceptions.ffmpeg.CannotCropVideoException
import com.silleruss.gotanda.models.FfmpegEncodeVideoRequest
import com.silleruss.gotanda.modules.crop.CropVideoPayload
import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class FfmpegService(
    @Value("\${ffmpeg.path}") private val path: String,
    @Value("\${ffprobe.path}") private val provePath: String,
) {

    private val ffmpeg = FFmpeg(path)
    private val ffProbe = FFprobe(provePath)

    fun execute(payload: CropVideoPayload): Either<Throwable, Unit> {
        return either {
            ensure(payload.canCropVideo()) {
                CannotCropVideoException(payload)
            }

            val executor = FFmpegExecutor(ffmpeg, ffProbe)
            val builder = build(FFmpegBuilder(),payload)

            executor.createJob(builder).run()
        }
    }

    fun execute(request: FfmpegEncodeVideoRequest) {
        val executor = FFmpegExecutor(ffmpeg, ffProbe)
        val builder = build(FFmpegBuilder(), request)

        executor.createJob(builder).run()
    }

    private fun build(
        builder: FFmpegBuilder,
        payload: CropVideoPayload,
    ): FFmpegBuilder {
        return payload.run {
            builder
                .setInput(payload.inputUrl)
                .overrideOutputFiles(true)
                .addOutput("${payload.outputPath}.${payload.fileFormat}")
                .setFormat(payload.fileFormat)
                .addExtraArgs("-ss", payload.startTime.toString())
                .addExtraArgs("-t", payload.durationTime.toString())
                .done()
        }
    }

    private fun build(
        builder: FFmpegBuilder,
        request: FfmpegEncodeVideoRequest,
    ): FFmpegBuilder {
        return request.run {
            builder
                .setInput(input)
                .overrideOutputFiles(shouldOverride)
                .addOutput(output)
                .setFormat(fileFormat)
                .setTargetSize(targetSize)
                .let { if (shouldDisableSubtitle) it.disableSubtitle() else it }
                .setAudioChannels(audioChannel)
                .setAudioCodec(audioCode)
                .setAudioSampleRate(audioSampleRate)
                .setAudioBitRate(audioBitRate)
                .setVideoCodec(videoCode)
                .setVideoFrameRate(videoFrameRate, videoFrameRatePer)
                .setVideoResolution(videoWidth, videoHeight)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done()
        }
    }

    companion object {

        // TODO: will be change to config values
        private const val FFMPEG_PATH = ""
        private const val FF_PROBE_PATH = ""

    }

}