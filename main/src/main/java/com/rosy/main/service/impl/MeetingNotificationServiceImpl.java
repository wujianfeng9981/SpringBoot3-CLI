package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.constant.CommonConstant;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.SqlUtils;
import com.rosy.common.utils.StringUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.meetingnotification.MeetingNotificationQueryRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.entity.MeetingNotification;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingNotificationVO;
import com.rosy.main.mapper.MeetingBookingMapper;
import com.rosy.main.mapper.MeetingNotificationMapper;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IMeetingNotificationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会议通知表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class MeetingNotificationServiceImpl extends ServiceImpl<MeetingNotificationMapper, MeetingNotification> implements IMeetingNotificationService {

    @Resource
    private MeetingBookingMapper meetingBookingMapper;

    @Resource
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    public boolean sendApprovalNotification(Long bookingId, Long userId, Byte status, String comment) {
        MeetingBooking booking = meetingBookingMapper.selectById(bookingId);
        if (booking == null) {
            return false;
        }

        MeetingNotification notification = new MeetingNotification();
        notification.setBookingId(bookingId);
        notification.setUserId(userId);
        notification.setType((byte) 1); // 审批结果通知

        String statusText = status == 1 ? "已通过" : "已驳回";
        notification.setTitle("预约" + statusText);
        notification.setContent(String.format("您的会议室预约「%s」%s。%s",
                booking.getTitle(),
                statusText,
                StringUtils.isNotBlank(comment) ? "审批意见：" + comment : ""));

        return this.save(notification);
    }

    @Override
    public boolean sendMeetingReminder(Long bookingId, Long userId) {
        MeetingBooking booking = meetingBookingMapper.selectById(bookingId);
        if (booking == null) {
            return false;
        }

        MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
        String roomName = room != null ? room.getName() : "未知会议室";

        MeetingNotification notification = new MeetingNotification();
        notification.setBookingId(bookingId);
        notification.setUserId(userId);
        notification.setType((byte) 2); // 会议开始前通知
        notification.setTitle("会议即将开始");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日 HH:mm");
        String startTimeStr = booking.getStartTime().format(formatter);

        notification.setContent(String.format("您预约的会议「%s」即将在 %s 于 %s 开始，请准时参加。",
                booking.getTitle(),
                startTimeStr,
                roomName));

        return this.save(notification);
    }

    @Override
    public boolean markAsRead(Long id) {
        MeetingNotification notification = this.getById(id);
        ThrowUtils.throwIf(notification == null, ErrorCode.NOT_FOUND_ERROR);

        notification.setIsRead((byte) 1);
        notification.setReadTime(LocalDateTime.now());
        return this.updateById(notification);
    }

    @Override
    public MeetingNotificationVO getNotificationVO(MeetingNotification notification) {
        if (notification == null) {
            return null;
        }
        MeetingNotificationVO notificationVO = new MeetingNotificationVO();
        BeanUtil.copyProperties(notification, notificationVO);
        return notificationVO;
    }

    @Override
    public Page<MeetingNotificationVO> getNotificationVOPage(Page<MeetingNotification> page) {
        List<MeetingNotification> records = page.getRecords();
        Page<MeetingNotificationVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<MeetingNotificationVO> voList = records.stream()
                .map(this::getNotificationVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<MeetingNotification> getQueryWrapper(MeetingNotificationQueryRequest queryRequest) {
        LambdaQueryWrapper<MeetingNotification> queryWrapper = new LambdaQueryWrapper<>();
        if (queryRequest == null) {
            return queryWrapper;
        }

        Long id = queryRequest.getId();
        Long bookingId = queryRequest.getBookingId();
        Long userId = queryRequest.getUserId();
        Byte type = queryRequest.getType();
        Byte isRead = queryRequest.getIsRead();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();

        queryWrapper.eq(id != null, MeetingNotification::getId, id);
        queryWrapper.eq(bookingId != null, MeetingNotification::getBookingId, bookingId);
        queryWrapper.eq(userId != null, MeetingNotification::getUserId, userId);
        queryWrapper.eq(type != null, MeetingNotification::getType, type);
        queryWrapper.eq(isRead != null, MeetingNotification::getIsRead, isRead);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder),
                MeetingNotification::getCreateTime);
        return queryWrapper;
    }
}
