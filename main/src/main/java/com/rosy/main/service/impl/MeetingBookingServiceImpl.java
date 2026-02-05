package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.MeetingBookingAddRequest;
import com.rosy.main.domain.dto.MeetingBookingUpdateRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingBookingVO;
import com.rosy.main.mapper.MeetingBookingMapper;
import com.rosy.main.service.IMeetingBookingService;
import com.rosy.main.service.IMeetingRoomService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingBookingServiceImpl extends ServiceImpl<MeetingBookingMapper, MeetingBooking> implements IMeetingBookingService {

    @Resource
    private IMeetingRoomService meetingRoomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBooking(MeetingBookingAddRequest request) {
        MeetingRoom room = meetingRoomService.getById(request.getRoomId());
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        ThrowUtils.throwIf(room.getStatus() != 1, ErrorCode.OPERATION_ERROR, "会议室不可用");
        ThrowUtils.throwIf(request.getStartTime().isAfter(request.getEndTime()), ErrorCode.PARAMS_ERROR, "开始时间不能晚于结束时间");
        ThrowUtils.throwIf(request.getStartTime().isBefore(LocalDateTime.now()), ErrorCode.PARAMS_ERROR, "预约时间不能早于当前时间");
        List<MeetingBooking> conflicts = checkConflict(request.getRoomId(), request.getStartTime(), request.getEndTime(), null);
        ThrowUtils.throwIf(!conflicts.isEmpty(), ErrorCode.OPERATION_ERROR, "该时间段已被预约");
        MeetingBooking booking = new MeetingBooking();
        BeanUtil.copyProperties(request, booking);
        booking.setStatus((byte) 0);
        boolean result = this.save(booking);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "预约失败");
        return booking.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBooking(MeetingBookingUpdateRequest request) {
        MeetingBooking booking = this.getById(request.getId());
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        ThrowUtils.throwIf(booking.getStatus() != 0, ErrorCode.OPERATION_ERROR, "只能修改待审批的预约");
        if (request.getRoomId() != null) {
            MeetingRoom room = meetingRoomService.getById(request.getRoomId());
            ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
            ThrowUtils.throwIf(room.getStatus() != 1, ErrorCode.OPERATION_ERROR, "会议室不可用");
        }
        LocalDateTime newStartTime = request.getStartTime() != null ? request.getStartTime() : booking.getStartTime();
        LocalDateTime newEndTime = request.getEndTime() != null ? request.getEndTime() : booking.getEndTime();
        Long newRoomId = request.getRoomId() != null ? request.getRoomId() : booking.getRoomId();
        List<MeetingBooking> conflicts = checkConflict(newRoomId, newStartTime, newEndTime, booking.getId());
        ThrowUtils.throwIf(!conflicts.isEmpty(), ErrorCode.OPERATION_ERROR, "该时间段已被预约");
        BeanUtil.copyProperties(request, booking, "id", "status", "createTime");
        boolean result = this.updateById(booking);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新预约失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelBooking(Long id) {
        MeetingBooking booking = this.getById(id);
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        booking.setStatus((byte) 3);
        boolean result = this.updateById(booking);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "取消预约失败");
    }

    @Override
    public MeetingBookingVO getBookingById(Long id) {
        MeetingBooking booking = this.getById(id);
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        MeetingBookingVO vo = new MeetingBookingVO();
        BeanUtil.copyProperties(booking, vo);
        MeetingRoom room = meetingRoomService.getById(booking.getRoomId());
        if (room != null) {
            vo.setRoomName(room.getName());
        }
        return vo;
    }

    @Override
    public Page<MeetingBookingVO> listBookings(int current, int size, Long roomId, Long applicantId, Byte status) {
        Page<MeetingBooking> page = new Page<>(current, size);
        LambdaQueryWrapper<MeetingBooking> wrapper = new LambdaQueryWrapper<>();
        if (roomId != null) {
            wrapper.eq(MeetingBooking::getRoomId, roomId);
        }
        if (applicantId != null) {
            wrapper.eq(MeetingBooking::getApplicantId, applicantId);
        }
        if (status != null) {
            wrapper.eq(MeetingBooking::getStatus, status);
        }
        wrapper.orderByDesc(MeetingBooking::getCreateTime);
        Page<MeetingBooking> resultPage = this.page(page, wrapper);
        Page<MeetingBookingVO> voPage = new Page<>(current, size, resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(booking -> {
            MeetingBookingVO vo = new MeetingBookingVO();
            BeanUtil.copyProperties(booking, vo);
            MeetingRoom room = meetingRoomService.getById(booking.getRoomId());
            if (room != null) {
                vo.setRoomName(room.getName());
            }
            return vo;
        }).toList());
        return voPage;
    }

    @Override
    public List<MeetingBooking> checkConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId) {
        return this.baseMapper.findConflictingBookings(roomId, startTime, endTime, excludeBookingId);
    }
}
