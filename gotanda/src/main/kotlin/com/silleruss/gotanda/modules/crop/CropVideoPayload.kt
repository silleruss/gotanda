package com.silleruss.gotanda.modules.crop

data class CropVideoPayload(
    val inputUrl: String,
    val outputPath: String,
    val startTime: Float,
    val durationTime: Float,
    val fileFormat: String,
) {

    fun canCropVideo(): Boolean {
        return when {
            inputUrl.isBlank() || fileFormat.isBlank()
                    || startTime.isNaN() || durationTime.isNaN() || durationTime == 0f -> false
            else -> true
        }
    }

}
