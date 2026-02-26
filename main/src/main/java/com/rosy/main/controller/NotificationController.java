package com.rosy.main.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.NotificationVO;
import com.rosy.main.service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "通知管理", description = "通知相关接口，包括获取通知、标记已读等")
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "获取我的通知", description = "获取当前用户的所有通知")
    @GetMapping("/my")
    public ApiResponse<List<NotificationVO>> getMyNotifications(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        List<NotificationVO> list = notificationService.getMyNotifications(userId);
        return ApiResponse.success(list);
    }

    @Operation(summary = "获取未读通知数", description = "获取用户的未读通知数量")
    @GetMapping("/unread/{userId}")
    public ApiResponse<Integer> getUnreadCount(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        int count = notificationService.getUnreadCount(userId);
        return ApiResponse.success(count);
    }

    @Operation(summary = "标记通知已读", description = "将指定通知标记为已读状态")
    @PutMapping("/read/{id}")
    public ApiResponse<Boolean> markAsRead(
            @Parameter(description = "通知ID") @PathVariable Long id,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        notificationService.markAsRead(id, userId);
        return ApiResponse.success(true);
    }

    @Operation(summary = "标记全部已读", description = "将用户的所有通知标记为已读状态")
    @PutMapping("/read/all")
    public ApiResponse<Boolean> markAllAsRead(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return ApiResponse.success(true);
    }
}
