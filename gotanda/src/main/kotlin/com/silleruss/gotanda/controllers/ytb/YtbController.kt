package com.silleruss.gotanda.controllers.ytb

import com.silleruss.gotanda.controllers.ytb.requests.CropYtbVideoRequest
import com.silleruss.gotanda.controllers.ytb.requests.GetYtbVideoDetailRequest
import com.silleruss.gotanda.controllers.ytb.requests.GetYtbVideoFormatRequest
import com.silleruss.gotanda.controllers.ytb.responses.CropYtbVideoResponse
import com.silleruss.gotanda.controllers.ytb.responses.GetYtbVideoDetailResponse
import com.silleruss.gotanda.controllers.ytb.responses.GetYtbVideoFormatsResponse
import com.silleruss.gotanda.modules.ytb.YtbService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ytb")
class YtbController(
    private val service: YtbService,
) {

    @GetMapping("/video/details")
    fun getVideoDetail(@RequestBody request: GetYtbVideoDetailRequest): GetYtbVideoDetailResponse {
        return service.getVideoDetail(request)
    }

    @GetMapping("/video/formats")
    fun getVideoFormats(@RequestBody request: GetYtbVideoFormatRequest): GetYtbVideoFormatsResponse {
        return service.getVideoFormats(request)
    }

    @PostMapping("/video/crop")
    suspend fun cropVideo(@RequestBody request: CropYtbVideoRequest): CropYtbVideoResponse {
        return service.cropVideo(request)
    }

}