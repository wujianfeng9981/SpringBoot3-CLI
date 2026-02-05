package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.constant.CommonConstant;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.SqlUtils;
import com.rosy.common.utils.StringUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomQueryRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IMeetingRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会议室表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    @Override
    public Long addMeetingRoom(MeetingRoomAddRequest addRequest) {
        MeetingRoom meetingRoom = new MeetingRoom();
        BeanUtil.copyProperties(addRequest, meetingRoom);
        // 默认启用
        if (meetingRoom.getStatus() == null) {
            meetingRoom.setStatus((byte) 1);
        }
        boolean result = this.save(meetingRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return meetingRoom.getId();
    }

    @Override
    public boolean updateMeetingRoom(MeetingRoomUpdateRequest updateRequest) {
        MeetingRoom meetingRoom = new MeetingRoom();
        BeanUtil.copyProperties(updateRequest, meetingRoom);
        boolean result = this.updateById(meetingRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public MeetingRoomVO getMeetingRoomVO(MeetingRoom meetingRoom) {
        if (meetingRoom == null) {
            return null;
        }
        MeetingRoomVO meetingRoomVO = new MeetingRoomVO();
        BeanUtil.copyProperties(meetingRoom, meetingRoomVO);
        return meetingRoomVO;
    }

    @Override
    public Page<MeetingRoomVO> getMeetingRoomVOPage(Page<MeetingRoom> page) {
        List<MeetingRoom> records = page.getRecords();
        Page<MeetingRoomVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<MeetingRoomVO> voList = records.stream()
                .map(this::getMeetingRoomVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<MeetingRoom> getQueryWrapper(MeetingRoomQueryRequest queryRequest) {
        LambdaQueryWrapper<MeetingRoom> queryWrapper = new LambdaQueryWrapper<>();
        if (queryRequest == null) {
            return queryWrapper;
        }
        // 从 queryRequest 中提取查询条件
        Long id = queryRequest.getId();
        String name = queryRequest.getName();
        String location = queryRequest.getLocation();
        Integer minCapacity = queryRequest.getMinCapacity();
        Integer maxCapacity = queryRequest.getMaxCapacity();
        Byte status = queryRequest.getStatus();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(id != null, MeetingRoom::getId, id);
        queryWrapper.like(StringUtils.isNotBlank(name), MeetingRoom::getName, name);
        queryWrapper.like(StringUtils.isNotBlank(location), MeetingRoom::getLocation, location);
        queryWrapper.ge(minCapacity != null, MeetingRoom::getCapacity, minCapacity);
        queryWrapper.le(maxCapacity != null, MeetingRoom::getCapacity, maxCapacity);
        queryWrapper.eq(status != null, MeetingRoom::getStatus, status);
        // 排序
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder),
                MeetingRoom::getCreateTime);
        return queryWrapper;
    }
}
