package com.silleruss.gotanda.controllers.ytb

import com.silleruss.gotanda.controllers.ytb.requests.GetYtbVideoDetailRequest
import com.silleruss.gotanda.controllers.ytb.responses.GetYtbVideoDetailResponse
import com.silleruss.gotanda.modules.ytb.YtbService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ytb")
class YtbController(
    private val service: YtbService,
) {

    @GetMapping("/video/details")
    fun getVideoDetail(@RequestBody request: GetYtbVideoDetailRequest): GetYtbVideoDetailResponse {
        return service.getVideoDetail(request)
    }

}