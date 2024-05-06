package com.silleruss.gotanda.controllers.ytb.requests

data class GetYtbVideoDetailRequest(
    val url: String,
)

data class GetYtbVideoFormatRequest(
    val url: String
)

data class CropYtbVideoRequest(
    val url: String,
    val startTime: Float,
    val durationTime: Float,
    // TODO: set enum class
    val fileFormat: String,
)