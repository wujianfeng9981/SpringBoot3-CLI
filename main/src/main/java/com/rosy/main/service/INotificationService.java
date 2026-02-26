package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.vo.NotificationVO;

import java.util.List;

public interface INotificationService extends IService<Notification> {

    void sendApprovalNotification(Booking booking, boolean isApproved);

    void sendMeetingReminderNotification(Booking booking);

    List<NotificationVO> getMyNotifications(Long userId);

    void markAsRead(Long notificationId, Long userId);

    void markAllAsRead(Long userId);

    int getUnreadCount(Long userId);
}
