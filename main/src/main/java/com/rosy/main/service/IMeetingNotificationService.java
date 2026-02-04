package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.meetingnotification.MeetingNotificationQueryRequest;
import com.rosy.main.domain.entity.MeetingNotification;
import com.rosy.main.domain.vo.MeetingNotificationVO;

public interface IMeetingNotificationService extends IService<MeetingNotification> {

    void createApprovalNotification(Long bookingId, Long userId, Byte approvalStatus);

    void createMeetingReminder(Long bookingId, Long userId);

    MeetingNotificationVO getNotificationVO(MeetingNotification notification);

    Page<MeetingNotificationVO> getNotificationVOPage(Page<MeetingNotification> page);

    LambdaQueryWrapper<MeetingNotification> getQueryWrapper(MeetingNotificationQueryRequest queryRequest);

    boolean markAsRead(Long notificationId, Long userId);

    boolean markAllAsRead(Long userId);

    long getUnreadCount(Long userId);
}
