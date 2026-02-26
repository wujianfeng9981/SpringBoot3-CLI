package com.rosy.main.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.service.ICheckInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会议签到管理", description = "会议签到相关接口")
@RestController
@RequestMapping("/api/check-in")
public class CheckInController {

    private final ICheckInService checkInService;

    public CheckInController(ICheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @Operation(summary = "执行签到", description = "对指定预约进行签到操作")
    @PostMapping
    public ApiResponse<Void> doCheckIn(
            @Parameter(description = "预约ID") @RequestParam Long bookingId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        checkInService.doCheckIn(bookingId, userId);
        return ApiResponse.success();
    }

    @Operation(summary = "获取签到人数", description = "获取指定预约的签到人数")
    @GetMapping("/count/{bookingId}")
    public ApiResponse<Integer> getCheckInCount(
            @Parameter(description = "预约ID") @PathVariable Long bookingId) {
        int count = checkInService.getCheckInCount(bookingId);
        return ApiResponse.success(count);
    }
}
