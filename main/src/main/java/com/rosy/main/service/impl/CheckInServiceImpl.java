package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.BookingStatusEnum;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.entity.CheckIn;
import com.rosy.main.mapper.CheckInMapper;
import com.rosy.main.service.IBookingService;
import com.rosy.main.service.ICheckInService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements ICheckInService {

    private final IBookingService bookingService;

    public CheckInServiceImpl(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void doCheckIn(Long bookingId, Long userId) {
        Booking booking = bookingService.getById(bookingId);
        if (booking == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        }

        if (booking.getStatus() != BookingStatusEnum.APPROVED.getCode()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只有已通过的预约才能签到");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(booking.getStartTime().minusMinutes(30))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "只能在会议开始前30分钟内签到");
        }
        if (now.isAfter(booking.getEndTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "会议已结束，无法签到");
        }

        QueryWrapper<CheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("booking_id", bookingId);
        queryWrapper.eq("user_id", userId);
        CheckIn existCheckIn = this.getOne(queryWrapper);
        if (existCheckIn != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "您已签到");
        }

        CheckIn checkIn = new CheckIn();
        checkIn.setBookingId(bookingId);
        checkIn.setUserId(userId);
        checkIn.setCheckInTime(LocalDateTime.now());
        this.save(checkIn);
    }

    @Override
    public int getCheckInCount(Long bookingId) {
        QueryWrapper<CheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("booking_id", bookingId);
        return (int) this.count(queryWrapper);
    }
}
