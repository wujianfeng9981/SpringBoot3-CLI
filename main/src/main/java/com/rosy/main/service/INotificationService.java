package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.vo.NotificationVO;

import java.time.LocalDateTime;

public interface INotificationService extends IService<Notification> {

    NotificationVO getNotificationVO(Notification notification);

    void sendApprovalNotification(Long userId, Long reservationId, boolean approved, String rejectReason);

    void sendMeetingReminder(Long userId, Long reservationId, String meetingTitle, LocalDateTime startTime);

    void markAsRead(Long notificationId);

    LambdaQueryWrapper<Notification> getQueryWrapper(Long userId, Byte isRead);
}
