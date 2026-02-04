package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinAddRequest;
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinQueryRequest;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.vo.MeetingCheckinVO;
import com.rosy.main.domain.vo.MeetingRoomStatisticsVO;
import com.rosy.main.service.IMeetingCheckinService;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/meeting-checkin")
public class MeetingCheckinController {

    @Resource
    private IMeetingCheckinService meetingCheckinService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse checkin(@RequestBody MeetingCheckinAddRequest request) {
        meetingCheckinService.checkin(request);
        return ApiResponse.success();
    }

    @GetMapping("/get")
    public ApiResponse getCheckinById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingCheckin checkin = meetingCheckinService.getById(id);
        ThrowUtils.throwIf(checkin == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(checkin);
    }

    @GetMapping("/get/vo")
    public ApiResponse getCheckinVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingCheckin checkin = meetingCheckinService.getById(id);
        ThrowUtils.throwIf(checkin == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(meetingCheckinService.getCheckinVO(checkin));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listCheckinByPage(@RequestBody MeetingCheckinQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<MeetingCheckin> page = meetingCheckinService.page(
                new Page<>(current, size),
                meetingCheckinService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listCheckinVOByPage(@RequestBody MeetingCheckinQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MeetingCheckin> page = meetingCheckinService.page(
                new Page<>(current, size),
                meetingCheckinService.getQueryWrapper(queryRequest)
        );
        Page<MeetingCheckinVO> voPage = meetingCheckinService.getCheckinVOPage(page);
        return ApiResponse.success(voPage);
    }

    @GetMapping("/statistics")
    public ApiResponse getRoomStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<MeetingRoomStatisticsVO> statistics = meetingCheckinService.getRoomStatistics(startTime, endTime);
        return ApiResponse.success(statistics);
    }
}
