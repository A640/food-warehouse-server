package foodwarehouse.web.controller;

import foodwarehouse.web.common.SuccessResponse;
import foodwarehouse.web.response.others.PingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {

    @GetMapping
    public SuccessResponse<PingResponse> getPing() {
        return new SuccessResponse<>(
                new PingResponse(true)
        );
    }
}