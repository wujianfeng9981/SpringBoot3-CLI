package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.MeetingNotification;
import com.rosy.main.mapper.MeetingNotificationMapper;
import com.rosy.main.service.IMeetingNotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MeetingNotificationServiceImpl extends ServiceImpl<MeetingNotificationMapper, MeetingNotification> implements IMeetingNotificationService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotification(Long userId, Long bookingId, Byte type, String title, String content) {
        MeetingNotification notification = new MeetingNotification();
        notification.setUserId(userId);
        notification.setBookingId(bookingId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead((byte) 0);
        boolean result = this.save(notification);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建通知失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long id) {
        MeetingNotification notification = this.getById(id);
        ThrowUtils.throwIf(notification == null, ErrorCode.NOT_FOUND_ERROR, "通知不存在");
        notification.setIsRead((byte) 1);
        notification.setReadTime(LocalDateTime.now());
        boolean result = this.updateById(notification);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "标记已读失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(Long userId) {
        LambdaQueryWrapper<MeetingNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingNotification::getUserId, userId)
                .eq(MeetingNotification::getIsRead, 0);
        MeetingNotification update = new MeetingNotification();
        update.setIsRead((byte) 1);
        update.setReadTime(LocalDateTime.now());
        this.update(update, wrapper);
    }

    @Override
    public Page<MeetingNotification> listNotifications(int current, int size, Long userId, Byte isRead) {
        Page<MeetingNotification> page = new Page<>(current, size);
        LambdaQueryWrapper<MeetingNotification> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(MeetingNotification::getUserId, userId);
        }
        if (isRead != null) {
            wrapper.eq(MeetingNotification::getIsRead, isRead);
        }
        wrapper.orderByDesc(MeetingNotification::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public int countUnread(Long userId) {
        LambdaQueryWrapper<MeetingNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingNotification::getUserId, userId)
                .eq(MeetingNotification::getIsRead, 0);
        return (int) this.count(wrapper);
    }
}
