package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinAddRequest;
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinQueryRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.vo.MeetingCheckinVO;
import com.rosy.main.domain.vo.MeetingRoomStatisticsVO;
import com.rosy.main.mapper.MeetingCheckinMapper;
import com.rosy.main.service.IMeetingBookingService;
import com.rosy.main.service.IMeetingCheckinService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingCheckinServiceImpl extends ServiceImpl<MeetingCheckinMapper, MeetingCheckin> implements IMeetingCheckinService {

    @Resource
    @Lazy
    private IMeetingBookingService meetingBookingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkin(MeetingCheckinAddRequest request) {
        MeetingBooking booking = meetingBookingService.getById(request.getBookingId());
        if (booking == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "预约记录不存在");
        }

        if (booking.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "预约未通过审批，不能签到");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(booking.getStartTime().minusMinutes(30))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "签到时间未到，提前30分钟开始签到");
        }

        if (now.isAfter(booking.getEndTime())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "会议已结束，不能签到");
        }

        LambdaQueryWrapper<MeetingCheckin> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(MeetingCheckin::getBookingId, request.getBookingId());
        checkWrapper.eq(MeetingCheckin::getUserId, request.getUserId());
        MeetingCheckin existingCheckin = this.getOne(checkWrapper);
        if (existingCheckin != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已签到，不能重复签到");
        }

        MeetingCheckin checkin = new MeetingCheckin();
        checkin.setBookingId(request.getBookingId());
        checkin.setUserId(request.getUserId());
        checkin.setCheckinTime(now);

        boolean result = this.save(checkin);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "签到失败");
        }

    }

    @Override
    public MeetingCheckinVO getCheckinVO(MeetingCheckin checkin) {
        return Optional.ofNullable(checkin)
                .map(c -> {
                    MeetingCheckinVO vo = BeanUtil.copyProperties(c, MeetingCheckinVO.class);
                    MeetingBooking booking = meetingBookingService.getById(c.getBookingId());
                    if (booking != null) {
                        vo.setMeetingTitle(booking.getTitle());
                    }
                    return vo;
                })
                .orElse(null);
    }

    @Override
    public Page<MeetingCheckinVO> getCheckinVOPage(Page<MeetingCheckin> page) {
        List<MeetingCheckinVO> voList = page.getRecords().stream()
                .map(this::getCheckinVO)
                .collect(Collectors.toList());

        Page<MeetingCheckinVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<MeetingCheckin> getQueryWrapper(MeetingCheckinQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        LambdaQueryWrapper<MeetingCheckin> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), MeetingCheckin::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getBookingId(), MeetingCheckin::getBookingId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), MeetingCheckin::getUserId);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                MeetingCheckin::getCheckinTime);

        return queryWrapper;
    }

    @Override
    public List<MeetingRoomStatisticsVO> getRoomStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.getRoomStatistics(startTime, endTime);
    }
}
