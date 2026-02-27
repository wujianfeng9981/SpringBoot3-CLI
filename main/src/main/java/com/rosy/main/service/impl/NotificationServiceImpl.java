package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.enums.NotificationType;
import com.rosy.main.domain.vo.NotificationVO;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.INotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    public NotificationVO getNotificationVO(Notification notification) {
        return Optional.ofNullable(notification)
                .map(n -> {
                    NotificationVO vo = BeanUtil.copyProperties(n, NotificationVO.class);
                    NotificationType type = NotificationType.getByCode(n.getType());
                    if (type != null) {
                        vo.setTypeDesc(type.getDesc());
                    }
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public void sendApprovalNotification(Long userId, Long reservationId, boolean approved, String rejectReason) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setReservationId(reservationId);
        notification.setType(NotificationType.APPROVAL_RESULT.getCode());
        notification.setTitle("预约审批结果通知");
        if (approved) {
            notification.setContent("您的会议室预约已通过审批。");
        } else {
            notification.setContent("您的会议室预约已被驳回。原因：" + rejectReason);
        }
        notification.setIsRead((byte) 0);
        notification.setSendTime(LocalDateTime.now());
        save(notification);
    }

    @Override
    public void sendMeetingReminder(Long userId, Long reservationId, String meetingTitle, LocalDateTime startTime) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setReservationId(reservationId);
        notification.setType(NotificationType.MEETING_REMINDER.getCode());
        notification.setTitle("会议提醒");
        notification.setContent("您预约的会议\"" + meetingTitle + "\"将于" + startTime + "开始，请准时参加。");
        notification.setIsRead((byte) 0);
        notification.setSendTime(LocalDateTime.now());
        save(notification);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = getById(notificationId);
        if (notification != null) {
            notification.setIsRead((byte) 1);
            updateById(notification);
        }
    }

    @Override
    public LambdaQueryWrapper<Notification> getQueryWrapper(Long userId, Byte isRead) {
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq(Notification::getUserId, userId);
        }
        if (isRead != null) {
            queryWrapper.eq(Notification::getIsRead, isRead);
        }
        queryWrapper.orderByDesc(Notification::getCreateTime);
        return queryWrapper;
    }
}
