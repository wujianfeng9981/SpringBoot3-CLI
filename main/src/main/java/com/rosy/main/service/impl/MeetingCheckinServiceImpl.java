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
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinAddRequest;
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinQueryRequest;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.vo.MeetingCheckinVO;
import com.rosy.main.domain.vo.MeetingRoomStatisticsVO;
import com.rosy.main.mapper.MeetingBookingMapper;
import com.rosy.main.mapper.MeetingCheckinMapper;
import com.rosy.main.service.IMeetingCheckinService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会议签到表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class MeetingCheckinServiceImpl extends ServiceImpl<MeetingCheckinMapper, MeetingCheckin> implements IMeetingCheckinService {

    @Resource
    private MeetingBookingMapper meetingBookingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long checkin(MeetingCheckinAddRequest request) {
        // 查询预约
        MeetingBooking booking = meetingBookingMapper.selectById(request.getBookingId());
        ThrowUtils.throwIf(booking == null, ErrorCode.NOT_FOUND_ERROR, "预约不存在");
        ThrowUtils.throwIf(booking.getStatus() != 1, ErrorCode.OPERATION_ERROR, "预约未通过审批");

        // 检查是否已签到
        LambdaQueryWrapper<MeetingCheckin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MeetingCheckin::getBookingId, request.getBookingId());
        queryWrapper.eq(MeetingCheckin::getUserId, 1L); // 当前用户ID
        long count = this.count(queryWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.OPERATION_ERROR, "您已签到");

        MeetingCheckin checkin = new MeetingCheckin();
        BeanUtil.copyProperties(request, checkin);
        checkin.setUserId(1L); // 当前用户ID
        checkin.setCheckinTime(LocalDateTime.now());

        // 判断签到类型
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(booking.getStartTime())) {
            checkin.setCheckinType((byte) 1); // 正常签到
        } else if (now.isAfter(booking.getEndTime())) {
            checkin.setCheckinType((byte) 3); // 早退（会议已结束）
        } else {
            checkin.setCheckinType((byte) 2); // 迟到
        }

        boolean result = this.save(checkin);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return checkin.getId();
    }

    @Override
    public MeetingCheckinVO getCheckinVO(MeetingCheckin checkin) {
        if (checkin == null) {
            return null;
        }
        MeetingCheckinVO checkinVO = new MeetingCheckinVO();
        BeanUtil.copyProperties(checkin, checkinVO);

        // 查询预约信息
        MeetingBooking booking = meetingBookingMapper.selectById(checkin.getBookingId());
        if (booking != null) {
            checkinVO.setBookingTitle(booking.getTitle());
        }

        return checkinVO;
    }

    @Override
    public Page<MeetingCheckinVO> getCheckinVOPage(Page<MeetingCheckin> page) {
        List<MeetingCheckin> records = page.getRecords();
        Page<MeetingCheckinVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<MeetingCheckinVO> voList = records.stream()
                .map(this::getCheckinVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<MeetingCheckin> getQueryWrapper(MeetingCheckinQueryRequest queryRequest) {
        LambdaQueryWrapper<MeetingCheckin> queryWrapper = new LambdaQueryWrapper<>();
        if (queryRequest == null) {
            return queryWrapper;
        }

        Long id = queryRequest.getId();
        Long bookingId = queryRequest.getBookingId();
        Long userId = queryRequest.getUserId();
        Byte checkinType = queryRequest.getCheckinType();
        LocalDateTime checkinTimeBegin = queryRequest.getCheckinTimeBegin();
        LocalDateTime checkinTimeEnd = queryRequest.getCheckinTimeEnd();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();

        queryWrapper.eq(id != null, MeetingCheckin::getId, id);
        queryWrapper.eq(bookingId != null, MeetingCheckin::getBookingId, bookingId);
        queryWrapper.eq(userId != null, MeetingCheckin::getUserId, userId);
        queryWrapper.eq(checkinType != null, MeetingCheckin::getCheckinType, checkinType);
        queryWrapper.ge(checkinTimeBegin != null, MeetingCheckin::getCheckinTime, checkinTimeBegin);
        queryWrapper.le(checkinTimeEnd != null, MeetingCheckin::getCheckinTime, checkinTimeEnd);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder),
                MeetingCheckin::getCheckinTime);
        return queryWrapper;
    }

    @Override
    public List<MeetingRoomStatisticsVO> getRoomStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.selectRoomStatistics(startTime, endTime);
    }
}
