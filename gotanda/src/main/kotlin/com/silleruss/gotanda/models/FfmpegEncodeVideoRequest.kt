package com.silleruss.gotanda.models

data class FfmpegEncodeVideoRequest(
    val input: String,
    val output: String,
    val shouldOverride: Boolean,
    // TODO: with enum class
    val fileFormat: String,
    val targetSize: Long,
    val shouldDisableSubtitle: Boolean,
    // TODO: with enum class
    val audioChannel: Int,
    // TODO: with enum class
    val audioCode: String,
    val audioSampleRate: Int,
    val audioBitRate: Long,
    // TODO: with enum class
    val videoCode: String,
    val videoFrameRate: Int,
    val videoFrameRatePer: Int,
    val videoWidth: Int,
    val videoHeight: Int,
)
