package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.meetingnotification.MeetingNotificationQueryRequest;
import com.rosy.main.domain.entity.MeetingNotification;
import com.rosy.main.domain.vo.MeetingNotificationVO;
import com.rosy.main.service.IMeetingNotificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/meeting-notification")
public class MeetingNotificationController {

    @Resource
    private IMeetingNotificationService meetingNotificationService;

    @GetMapping("/get")
    public ApiResponse getNotificationById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingNotification notification = meetingNotificationService.getById(id);
        ThrowUtils.throwIf(notification == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(notification);
    }

    @GetMapping("/get/vo")
    public ApiResponse getNotificationVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingNotification notification = meetingNotificationService.getById(id);
        ThrowUtils.throwIf(notification == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(meetingNotificationService.getNotificationVO(notification));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listNotificationByPage(@RequestBody MeetingNotificationQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<MeetingNotification> page = meetingNotificationService.page(
                new Page<>(current, size),
                meetingNotificationService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listNotificationVOByPage(@RequestBody MeetingNotificationQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 50, ErrorCode.PARAMS_ERROR);
        Page<MeetingNotification> page = meetingNotificationService.page(
                new Page<>(current, size),
                meetingNotificationService.getQueryWrapper(queryRequest)
        );
        Page<MeetingNotificationVO> voPage = meetingNotificationService.getNotificationVOPage(page);
        return ApiResponse.success(voPage);
    }

    @PostMapping("/mark-read/{id}")
    public ApiResponse markAsRead(@PathVariable long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = 1L;
        boolean result = meetingNotificationService.markAsRead(id, userId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "标记已读失败");
        return ApiResponse.success(true);
    }

    @PostMapping("/mark-all-read")
    public ApiResponse markAllAsRead() {
        Long userId = 1L;
        boolean result = meetingNotificationService.markAllAsRead(userId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "批量标记已读失败");
        return ApiResponse.success(true);
    }

    @GetMapping("/unread/count")
    public ApiResponse getUnreadCount() {
        Long userId = 1L;
        long count = meetingNotificationService.getUnreadCount(userId);
        Map<String, Long> result = new HashMap<>();
        result.put("unreadCount", count);
        return ApiResponse.success(result);
    }
}
