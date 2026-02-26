package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.NotificationTypeEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.vo.NotificationVO;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.IMeetingRoomService;
import com.rosy.main.service.INotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    private final IMeetingRoomService meetingRoomService;

    public NotificationServiceImpl(IMeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @Override
    public void sendApprovalNotification(Booking booking, boolean isApproved) {
        Notification notification = new Notification();
        notification.setUserId(booking.getUserId());
        notification.setBookingId(booking.getId());
        notification.setType((byte) NotificationTypeEnum.APPROVAL_RESULT.getCode());

        MeetingRoom room = meetingRoomService.getById(booking.getRoomId());
        String roomName = room != null ? room.getName() : "未知会议室";
        String timeStr = booking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        if (isApproved) {
            notification.setTitle("预约审批通过");
            notification.setContent(String.format("您在 %s 的 %s 预约已通过审批，请准时参会。",
                    timeStr, roomName));
        } else {
            notification.setTitle("预约审批驳回");
            notification.setContent(String.format("您在 %s 的 %s 预约已被驳回。",
                    timeStr, roomName));
        }

        notification.setIsRead((byte) 0);
        notification.setSendTime(LocalDateTime.now());
        this.save(notification);
    }

    @Override
    public void sendMeetingReminderNotification(Booking booking) {
        Notification notification = new Notification();
        notification.setUserId(booking.getUserId());
        notification.setBookingId(booking.getId());
        notification.setType((byte) NotificationTypeEnum.MEETING_REMINDER.getCode());

        MeetingRoom room = meetingRoomService.getById(booking.getRoomId());
        String roomName = room != null ? room.getName() : "未知会议室";
        String timeStr = booking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        notification.setTitle("会议即将开始");
        notification.setContent(String.format("温馨提醒：您的会议 %s 将在15分钟后开始，地点：%s，请勿忘参加！",
                timeStr, roomName));

        notification.setIsRead((byte) 0);
        notification.setSendTime(LocalDateTime.now());
        this.save(notification);
    }

    @Override
    public List<NotificationVO> getMyNotifications(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("send_time");

        List<Notification> notifications = this.list(queryWrapper);
        return notifications.stream()
                .map(this::toNotificationVO)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = this.getById(notificationId);
        if (notification == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "通知不存在");
        }
        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        notification.setIsRead((byte) 1);
        this.updateById(notification);
    }

    @Override
    public int getUnreadCount(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_read", 0);
        return (int) this.count(queryWrapper);
    }

    @Override
    public void markAllAsRead(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_read", 0);
        
        Notification notification = new Notification();
        notification.setIsRead((byte) 1);
        this.update(notification, queryWrapper);
    }

    private NotificationVO toNotificationVO(Notification notification) {
        if (notification == null) return null;
        NotificationVO vo = new NotificationVO();
        vo.setId(notification.getId());
        vo.setUserId(notification.getUserId());
        vo.setBookingId(notification.getBookingId());
        vo.setType(notification.getType());
        vo.setTitle(notification.getTitle());
        vo.setContent(notification.getContent());
        vo.setIsRead(notification.getIsRead());
        vo.setSendTime(notification.getSendTime());

        for (NotificationTypeEnum type : NotificationTypeEnum.values()) {
            if (type.getCode() == notification.getType()) {
                vo.setTypeText(type.getText());
                break;
            }
        }
        return vo;
    }
}
