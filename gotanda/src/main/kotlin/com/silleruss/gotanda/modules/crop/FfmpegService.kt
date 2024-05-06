package com.silleruss.gotanda.modules.crop

import com.silleruss.gotanda.models.FfmpegEncodeVideoRequest
import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import org.springframework.stereotype.Service

@Service
class FfmpegService {

    private val ffmpeg = FFmpeg(FFMPEG_PATH)
    private val ffProbe = FFprobe(FF_PROBE_PATH)

    fun execute(payload: CropVideoPayload) {
        runCatching {
            val executor = FFmpegExecutor(ffmpeg, ffProbe)
            val builder = build(FFmpegBuilder(),payload)

            executor.createJob(builder).run()
        }.onFailure {
            // FIXME
            println("Failed: ${it.message}")
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