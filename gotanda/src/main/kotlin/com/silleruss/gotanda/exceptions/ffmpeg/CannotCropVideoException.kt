package com.silleruss.gotanda.exceptions.ffmpeg

import com.silleruss.gotanda.modules.crop.CropVideoPayload
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CannotCropVideoException(payload: CropVideoPayload) : RuntimeException("Requested payload: $payload")