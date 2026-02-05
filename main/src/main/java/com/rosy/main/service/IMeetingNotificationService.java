package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.MeetingNotification;

import java.util.List;

public interface IMeetingNotificationService extends IService<MeetingNotification> {

    void createNotification(Long userId, Long bookingId, Byte type, String title, String content);

    void markAsRead(Long id);

    void markAllAsRead(Long userId);

    Page<MeetingNotification> listNotifications(int current, int size, Long userId, Byte isRead);

    int countUnread(Long userId);
}
