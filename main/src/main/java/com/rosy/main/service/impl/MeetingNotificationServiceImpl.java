package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.meetingnotification.MeetingNotificationQueryRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.entity.MeetingNotification;
import com.rosy.main.domain.vo.MeetingNotificationVO;
import com.rosy.main.mapper.MeetingNotificationMapper;
import com.rosy.main.service.IMeetingBookingService;
import com.rosy.main.service.IMeetingNotificationService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingNotificationServiceImpl extends ServiceImpl<MeetingNotificationMapper, MeetingNotification> implements IMeetingNotificationService {

    @Resource
    @Lazy
    private IMeetingBookingService meetingBookingService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createApprovalNotification(Long bookingId, Long userId, Byte approvalStatus) {
        MeetingBooking booking = meetingBookingService.getById(bookingId);
        if (booking == null) {
            return;
        }

        String statusText = approvalStatus == 1 ? "已通过" : "已驳回";
        String content = String.format("您的会议预约【%s】%s", booking.getTitle(), statusText);

        MeetingNotification notification = new MeetingNotification();
        notification.setBookingId(bookingId);
        notification.setUserId(userId);
        notification.setType((byte) 1);
        notification.setContent(content);
        notification.setIsRead((byte) 0);

        this.save(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMeetingReminder(Long bookingId, Long userId) {
        MeetingBooking booking = meetingBookingService.getById(bookingId);
        if (booking == null) {
            return;
        }

        String timeStr = booking.getStartTime().format(FORMATTER);
        String content = String.format("会议提醒：【%s】将于 %s 开始，请准时参加", booking.getTitle(), timeStr);

        MeetingNotification notification = new MeetingNotification();
        notification.setBookingId(bookingId);
        notification.setUserId(userId);
        notification.setType((byte) 2);
        notification.setContent(content);
        notification.setIsRead((byte) 0);

        this.save(notification);
    }

    @Override
    public MeetingNotificationVO getNotificationVO(MeetingNotification notification) {
        return Optional.ofNullable(notification)
                .map(n -> {
                    MeetingNotificationVO vo = BeanUtil.copyProperties(n, MeetingNotificationVO.class);
                    MeetingBooking booking = meetingBookingService.getById(n.getBookingId());
                    if (booking != null) {
                        vo.setMeetingTitle(booking.getTitle());
                    }
                    vo.setTypeName(getTypeName(n.getType()));
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public Page<MeetingNotificationVO> getNotificationVOPage(Page<MeetingNotification> page) {
        List<MeetingNotificationVO> voList = page.getRecords().stream()
                .map(this::getNotificationVO)
                .collect(Collectors.toList());

        Page<MeetingNotificationVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<MeetingNotification> getQueryWrapper(MeetingNotificationQueryRequest queryRequest) {
        if (queryRequest == null) {
            return new LambdaQueryWrapper<>();
        }

        LambdaQueryWrapper<MeetingNotification> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), MeetingNotification::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getBookingId(), MeetingNotification::getBookingId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), MeetingNotification::getUserId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getType(), MeetingNotification::getType);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getIsRead(), MeetingNotification::getIsRead);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                MeetingNotification::getCreateTime);

        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long notificationId, Long userId) {
        MeetingNotification notification = this.getById(notificationId);
        if (notification == null || !notification.getUserId().equals(userId)) {
            return false;
        }

        notification.setIsRead((byte) 1);
        notification.setReadTime(LocalDateTime.now());

        return this.updateById(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAllAsRead(Long userId) {
        int count = baseMapper.markAllAsRead(userId);
        return count >= 0;
    }

    @Override
    public long getUnreadCount(Long userId) {
        LambdaQueryWrapper<MeetingNotification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MeetingNotification::getUserId, userId);
        queryWrapper.eq(MeetingNotification::getIsRead, 0);
        return this.count(queryWrapper);
    }

    private String getTypeName(Byte type) {
        if (type == null) {
            return "未知";
        }
        return switch (type) {
            case 1 -> "审批结果";
            case 2 -> "会议提醒";
            default -> "未知";
        };
    }
}
