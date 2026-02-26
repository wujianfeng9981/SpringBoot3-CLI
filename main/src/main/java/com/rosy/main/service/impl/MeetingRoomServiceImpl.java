package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomQueryRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IMeetingRoomService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    @Override
    public Long addRoom(MeetingRoomAddRequest request) {
        if (StringUtils.isBlank(request.getName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "会议室名称不能为空");
        }
        if (request.getCapacity() == null || request.getCapacity() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "容量必须大于0");
        }

        QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", request.getName());
        MeetingRoom existRoom = this.getOne(queryWrapper);
        if (existRoom != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "会议室名称已存在");
        }

        MeetingRoom room = BeanUtil.copyProperties(request, MeetingRoom.class);
        room.setStatus((byte) 1);
        this.save(room);
        return room.getId();
    }

    @Override
    public Boolean updateRoom(MeetingRoomUpdateRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom existRoom = this.getById(request.getId());
        if (existRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        }

        if (StringUtils.isNotBlank(request.getName()) && !request.getName().equals(existRoom.getName())) {
            QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", request.getName());
            MeetingRoom roomByName = this.getOne(queryWrapper);
            if (roomByName != null && !roomByName.getId().equals(request.getId())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "会议室名称已存在");
            }
        }

        MeetingRoom updateRoom = BeanUtil.copyProperties(request, MeetingRoom.class);
        return this.updateById(updateRoom);
    }

    @Override
    public Boolean deleteRoom(Long id) {
        MeetingRoom room = this.getById(id);
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        }
        return this.removeById(id);
    }

    @Override
    public MeetingRoomVO getRoomById(Long id) {
        MeetingRoom room = this.getById(id);
        return toMeetingRoomVO(room);
    }

    @Override
    public List<MeetingRoomVO> getRoomList(MeetingRoomQueryRequest request) {
        QueryWrapper<MeetingRoom> queryWrapper = getQueryWrapper(request);
        List<MeetingRoom> list = this.list(queryWrapper);
        return list.stream()
                .map(this::toMeetingRoomVO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MeetingRoomVO> getRoomPage(MeetingRoomQueryRequest request, Integer pageNum, Integer pageSize) {
        QueryWrapper<MeetingRoom> queryWrapper = getQueryWrapper(request);
        Page<MeetingRoom> page = this.page(
                new Page<>(pageNum != null ? pageNum : 1, pageSize != null ? pageSize : 10),
                queryWrapper
        );
        Page<MeetingRoomVO> resultPage = new Page<>();
        resultPage.setRecords(page.getRecords().stream()
                .map(this::toMeetingRoomVO)
                .collect(Collectors.toList()));
        resultPage.setTotal(page.getTotal());
        resultPage.setCurrent(page.getCurrent());
        resultPage.setSize(page.getSize());
        resultPage.setPages(page.getPages());
        return resultPage;
    }

    @Override
    public QueryWrapper<MeetingRoom> getQueryWrapper(MeetingRoomQueryRequest request) {
        QueryWrapper<MeetingRoom> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.like("name", request.getName());
        }
        if (StringUtils.isNotBlank(request.getLocation())) {
            queryWrapper.like("location", request.getLocation());
        }
        if (request.getMinCapacity() != null) {
            queryWrapper.ge("capacity", request.getMinCapacity());
        }
        if (StringUtils.isNotBlank(request.getEquipment())) {
            queryWrapper.like("equipment", request.getEquipment());
        }
        if (request.getStatus() != null) {
            queryWrapper.eq("status", request.getStatus());
        }

        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    @Override
    public MeetingRoomVO toMeetingRoomVO(MeetingRoom room) {
        return Optional.ofNullable(room)
                .map(r -> BeanUtil.copyProperties(r, MeetingRoomVO.class))
                .orElse(null);
    }
}
