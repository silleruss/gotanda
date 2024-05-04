package com.silleruss.gotanda.modules.crop

import com.silleruss.gotanda.models.FfmpegEncodeVideoRequest
import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import org.springframework.stereotype.Service

@Service
class FfmpegService {

    fun encodeVideo(request: FfmpegEncodeVideoRequest): Result<Unit> {
        return runCatching {
            val ffmpeg = FFmpeg(FFMPEG_PATH)
            val ffProbe = FFprobe(FF_PROBE_PATH)

            val builder = request.generate()

            val executor = FFmpegExecutor(ffmpeg, ffProbe)

            // Run a one-pass encode
            executor.createJob(builder).run()

            // Or run a two-pass encode (which is better quality at the cost of being slower)
            executor.createTwoPassJob(builder).run()
        }.onSuccess {
            // do something
        }
    }

    private fun FfmpegEncodeVideoRequest.generate(): FFmpegBuilder {
        return FFmpegBuilder()
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

    companion object {

        // TODO: will be change to config values
        private const val FFMPEG_PATH = ""
        private const val FF_PROBE_PATH = ""

    }

}