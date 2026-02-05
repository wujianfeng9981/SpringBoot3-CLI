package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.vo.MeetingCheckinVO;
import com.rosy.main.mapper.MeetingCheckinMapper;
import com.rosy.main.service.IMeetingBookingService;
import com.rosy.main.service.IMeetingCheckinService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MeetingCheckinServiceImpl extends ServiceImpl<MeetingCheckinMapper, MeetingCheckin> implements IMeetingCheckinService {

    @Resource
    private IMeetingBookingService meetingBookingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkin(Long bookingId, Long userId, String userName) {
        MeetingBooking booking = meetingBookingService.getById(bookingId);
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        ThrowUtils.throwIf(booking.getStatus() != 1, ErrorCode.OPERATION_ERROR, "只能签到已通过的预约");
        LocalDateTime now = LocalDateTime.now();
        ThrowUtils.throwIf(now.isBefore(booking.getStartTime().minusMinutes(30)), ErrorCode.OPERATION_ERROR, "签到时间未到");
        ThrowUtils.throwIf(now.isAfter(booking.getEndTime()), ErrorCode.OPERATION_ERROR, "会议已结束，无法签到");
        LambdaQueryWrapper<MeetingCheckin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingCheckin::getBookingId, bookingId)
                .eq(MeetingCheckin::getUserId, userId);
        MeetingCheckin existingCheckin = this.getOne(wrapper);
        ThrowUtils.throwIf(existingCheckin != null, ErrorCode.OPERATION_ERROR, "已签到");
        MeetingCheckin checkin = new MeetingCheckin();
        checkin.setBookingId(bookingId);
        checkin.setUserId(userId);
        checkin.setUserName(userName);
        checkin.setCheckinTime(now);
        boolean result = this.save(checkin);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "签到失败");
    }

    @Override
    public Page<MeetingCheckinVO> listCheckins(int current, int size, Long bookingId, Long userId) {
        Page<MeetingCheckin> page = new Page<>(current, size);
        LambdaQueryWrapper<MeetingCheckin> wrapper = new LambdaQueryWrapper<>();
        if (bookingId != null) {
            wrapper.eq(MeetingCheckin::getBookingId, bookingId);
        }
        if (userId != null) {
            wrapper.eq(MeetingCheckin::getUserId, userId);
        }
        wrapper.orderByDesc(MeetingCheckin::getCheckinTime);
        Page<MeetingCheckin> resultPage = this.page(page, wrapper);
        Page<MeetingCheckinVO> voPage = new Page<>(current, size, resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(checkin -> {
            MeetingCheckinVO vo = new MeetingCheckinVO();
            BeanUtil.copyProperties(checkin, vo);
            return vo;
        }).toList());
        return voPage;
    }

    @Override
    public int countCheckins(Long bookingId) {
        LambdaQueryWrapper<MeetingCheckin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingCheckin::getBookingId, bookingId);
        return (int) this.count(wrapper);
    }
}
