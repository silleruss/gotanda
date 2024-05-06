package com.silleruss.gotanda.modules.crop

data class CropVideoPayload(
    val inputUrl: String,
    val outputPath: String,
    val startTime: Float,
    val durationTime: Float,
    val fileFormat: String,
)
