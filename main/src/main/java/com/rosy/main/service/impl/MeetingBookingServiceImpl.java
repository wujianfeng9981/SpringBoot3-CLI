package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingAddRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingQueryRequest;
import com.rosy.main.domain.dto.meetingbooking.MeetingBookingUpdateRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingBookingVO;
import com.rosy.main.mapper.MeetingBookingMapper;
import com.rosy.main.service.IMeetingBookingService;
import com.rosy.main.service.IMeetingRoomService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingBookingServiceImpl extends ServiceImpl<MeetingBookingMapper, MeetingBooking> implements IMeetingBookingService {

    @Resource
    @Lazy
    private IMeetingRoomService meetingRoomService;

    private static final List<Byte> VALID_STATUSES_FOR_CONFLICT = Arrays.asList((byte) 0, (byte) 1, (byte) 2);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBooking(MeetingBookingAddRequest addRequest) {
        if (addRequest.getStartTime().isAfter(addRequest.getEndTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间不能晚于结束时间");
        }

        if (addRequest.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "预约时间不能早于当前时间");
        }

        MeetingRoom room = meetingRoomService.getById(addRequest.getRoomId());
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        }

        if (room.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "会议室不可用");
        }

        if (addRequest.getAttendees() != null && addRequest.getAttendees() > room.getCapacity()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参会人数超过会议室容量");
        }

        if (checkConflict(addRequest.getRoomId(), addRequest.getStartTime(), addRequest.getEndTime(), null)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该时间段已被预约");
        }

        MeetingBooking booking = BeanUtil.copyProperties(addRequest, MeetingBooking.class);
        booking.setStatus((byte) 0);

        boolean result = this.save(booking);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "预约失败");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBooking(MeetingBookingUpdateRequest updateRequest) {
        MeetingBooking existingBooking = this.getById(updateRequest.getId());
        if (existingBooking == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "预约记录不存在");
        }

        if (existingBooking.getStatus() == 3) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已取消的预约不能修改");
        }

        if (updateRequest.getStartTime() != null && updateRequest.getEndTime() != null) {
            if (updateRequest.getStartTime().isAfter(updateRequest.getEndTime())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "开始时间不能晚于结束时间");
            }

            if (checkConflict(
                    existingBooking.getRoomId(),
                    updateRequest.getStartTime(),
                    updateRequest.getEndTime(),
                    updateRequest.getId())) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "该时间段已被预约");
            }
        }

        if (updateRequest.getRoomId() != null && !updateRequest.getRoomId().equals(existingBooking.getRoomId())) {
            MeetingRoom room = meetingRoomService.getById(updateRequest.getRoomId());
            if (room == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
            }
            if (room.getStatus() != 1) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "会议室不可用");
            }

            LocalDateTime startTime = updateRequest.getStartTime() != null ? updateRequest.getStartTime() : existingBooking.getStartTime();
            LocalDateTime endTime = updateRequest.getEndTime() != null ? updateRequest.getEndTime() : existingBooking.getEndTime();

            if (checkConflict(updateRequest.getRoomId(), startTime, endTime, updateRequest.getId())) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "该时间段已被预约");
            }
        }

        MeetingBooking updateBooking = BeanUtil.copyProperties(updateRequest, MeetingBooking.class);
        boolean result = this.updateById(updateBooking);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }

    }

    @Override
    public MeetingBookingVO getBookingVO(MeetingBooking booking) {
        return Optional.ofNullable(booking)
                .map(b -> {
                    MeetingBookingVO vo = BeanUtil.copyProperties(b, MeetingBookingVO.class);
                    MeetingRoom room = meetingRoomService.getById(b.getRoomId());
                    if (room != null) {
                        vo.setRoomName(room.getName());
                    }
                    vo.setStatusName(getStatusName(b.getStatus()));
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public Page<MeetingBookingVO> getBookingVOPage(Page<MeetingBooking> page) {
        List<MeetingBookingVO> voList = page.getRecords().stream()
                .map(this::getBookingVO)
                .collect(Collectors.toList());

        Page<MeetingBookingVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<MeetingBooking> getQueryWrapper(MeetingBookingQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        LambdaQueryWrapper<MeetingBooking> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), MeetingBooking::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getRoomId(), MeetingBooking::getRoomId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), MeetingBooking::getUserId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getTitle(), MeetingBooking::getTitle);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), MeetingBooking::getStatus);

        if (queryRequest.getStartTimeStart() != null) {
            queryWrapper.ge(MeetingBooking::getStartTime, queryRequest.getStartTimeStart());
        }
        if (queryRequest.getStartTimeEnd() != null) {
            queryWrapper.le(MeetingBooking::getStartTime, queryRequest.getStartTimeEnd());
        }
        if (queryRequest.getEndTimeStart() != null) {
            queryWrapper.ge(MeetingBooking::getEndTime, queryRequest.getEndTimeStart());
        }
        if (queryRequest.getEndTimeEnd() != null) {
            queryWrapper.le(MeetingBooking::getEndTime, queryRequest.getEndTimeEnd());
        }

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                MeetingBooking::getCreateTime);

        return queryWrapper;
    }

    @Override
    public boolean checkConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        List<MeetingBooking> conflicts = baseMapper.checkConflict(roomId, startTime, endTime, excludeId);
        return conflicts != null && !conflicts.isEmpty();
    }

    private String getStatusName(Byte status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case 0 -> "待审批";
            case 1 -> "已通过";
            case 2 -> "已驳回";
            case 3 -> "已取消";
            default -> "未知";
        };
    }
}
