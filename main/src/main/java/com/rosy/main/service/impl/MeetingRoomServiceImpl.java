package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomQueryRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IMeetingRoomService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    @Override
    public MeetingRoomVO getMeetingRoomVO(MeetingRoom meetingRoom) {
        return Optional.ofNullable(meetingRoom)
                .map(room -> BeanUtil.copyProperties(room, MeetingRoomVO.class))
                .orElse(null);
    }

    @Override
    public LambdaQueryWrapper<MeetingRoom> getQueryWrapper(MeetingRoomQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<MeetingRoom> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), MeetingRoom::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getName(), MeetingRoom::getName);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getLocation(), MeetingRoom::getLocation);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getEquipment(), MeetingRoom::getEquipment);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), MeetingRoom::getStatus);

        if (queryRequest.getMinCapacity() != null) {
            queryWrapper.ge(MeetingRoom::getCapacity, queryRequest.getMinCapacity());
        }

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                MeetingRoom::getId);

        return queryWrapper;
    }
}
