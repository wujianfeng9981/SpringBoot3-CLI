package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.main.domain.dto.statistics.RoomUsageQueryRequest;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.vo.NotificationVO;
import com.rosy.main.domain.vo.RoomUsageStatisticsVO;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private IStatisticsService statisticsService;

    @Resource
    private INotificationService notificationService;

    @GetMapping("/room/usage")
    public ApiResponse getRoomUsageStatistics(Long roomId, RoomUsageQueryRequest queryRequest) {
        if (roomId == null || roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RoomUsageStatisticsVO statistics = statisticsService.getRoomUsageStatistics(roomId, queryRequest);
        return ApiResponse.success(statistics);
    }

    @PostMapping("/room/usage/all")
    @ValidateRequest
    public ApiResponse getAllRoomsUsageStatistics(@RequestBody RoomUsageQueryRequest queryRequest) {
        List<RoomUsageStatisticsVO> statistics = statisticsService.getAllRoomsUsageStatistics(queryRequest);
        return ApiResponse.success(statistics);
    }

    @GetMapping("/notification/list")
    public ApiResponse listNotifications(Long userId, Byte isRead, Long current, Long pageSize) {
        if (current == null) current = 1L;
        if (pageSize == null) pageSize = 10L;
        if (pageSize > 20) pageSize = 20L;

        Page<Notification> notificationPage = notificationService.page(
                new Page<>(current, pageSize),
                notificationService.getQueryWrapper(userId, isRead)
        );
        Page<NotificationVO> notificationVOPage = PageUtils.convert(notificationPage, notificationService::getNotificationVO);
        return ApiResponse.success(notificationVOPage);
    }

    @PostMapping("/notification/read")
    public ApiResponse markNotificationAsRead(Long notificationId) {
        if (notificationId == null || notificationId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        notificationService.markAsRead(notificationId);
        return ApiResponse.success(true);
    }

    @GetMapping("/notification/unread/count")
    public ApiResponse getUnreadNotificationCount(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long count = notificationService.count(notificationService.getQueryWrapper(userId, (byte) 0));
        return ApiResponse.success(count);
    }
}
