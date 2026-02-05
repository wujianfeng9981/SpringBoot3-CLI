package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.meetingnotification.MeetingNotificationQueryRequest;
import com.rosy.main.domain.entity.MeetingNotification;
import com.rosy.main.domain.vo.MeetingNotificationVO;
import com.rosy.main.service.IMeetingNotificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会议通知表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/meeting-notification")
public class MeetingNotificationController {
    @Resource
    IMeetingNotificationService meetingNotificationService;

    /**
     * 标记通知为已读
     */
    @PostMapping("/read")
    @ValidateRequest
    public ApiResponse markAsRead(@RequestBody IdRequest idRequest) {
        boolean result = meetingNotificationService.markAsRead(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public ApiResponse getNotificationById(@RequestParam("id") Long id) {
        MeetingNotification notification = meetingNotificationService.getById(id);
        ThrowUtils.throwIf(notification == null, ErrorCode.NOT_FOUND_ERROR);
        MeetingNotificationVO notificationVO = meetingNotificationService.getNotificationVO(notification);
        return ApiResponse.success(notificationVO);
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listNotificationByPage(@RequestBody MeetingNotificationQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MeetingNotification> notificationPage = meetingNotificationService.page(
                new Page<>(current, size),
                meetingNotificationService.getQueryWrapper(request)
        );
        Page<MeetingNotificationVO> notificationVOPage = meetingNotificationService.getNotificationVOPage(notificationPage);
        return ApiResponse.success(notificationVOPage);
    }
}
