package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
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

/**
 * <p>
 * 会议签到表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/meeting-checkin")
public class MeetingCheckinController {
    @Resource
    IMeetingCheckinService meetingCheckinService;

    /**
     * 签到
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse checkin(@RequestBody MeetingCheckinAddRequest request) {
        Long id = meetingCheckinService.checkin(request);
        return ApiResponse.success(id);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public ApiResponse getCheckinById(@RequestParam("id") Long id) {
        MeetingCheckin checkin = meetingCheckinService.getById(id);
        ThrowUtils.throwIf(checkin == null, ErrorCode.NOT_FOUND_ERROR);
        MeetingCheckinVO checkinVO = meetingCheckinService.getCheckinVO(checkin);
        return ApiResponse.success(checkinVO);
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listCheckinByPage(@RequestBody MeetingCheckinQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MeetingCheckin> checkinPage = meetingCheckinService.page(
                new Page<>(current, size),
                meetingCheckinService.getQueryWrapper(request)
        );
        Page<MeetingCheckinVO> checkinVOPage = meetingCheckinService.getCheckinVOPage(checkinPage);
        return ApiResponse.success(checkinVOPage);
    }

    /**
     * 获取会议室使用统计
     */
    @GetMapping("/statistics")
    public ApiResponse getRoomStatistics(
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<MeetingRoomStatisticsVO> statistics = meetingCheckinService.getRoomStatistics(startTime, endTime);
        return ApiResponse.success(statistics);
    }
}
