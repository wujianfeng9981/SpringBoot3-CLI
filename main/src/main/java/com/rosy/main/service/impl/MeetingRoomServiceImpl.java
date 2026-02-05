package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IMeetingRoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRoom(MeetingRoomAddRequest request) {
        MeetingRoom room = new MeetingRoom();
        BeanUtil.copyProperties(request, room);
        room.setStatus((byte) 1);
        boolean result = this.save(room);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "添加会议室失败");
        return room.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoom(MeetingRoomUpdateRequest request) {
        MeetingRoom room = this.getById(request.getId());
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        BeanUtil.copyProperties(request, room);
        boolean result = this.updateById(room);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新会议室失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoom(Long id) {
        MeetingRoom room = this.getById(id);
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        boolean result = this.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除会议室失败");
    }

    @Override
    public MeetingRoomVO getRoomById(Long id) {
        MeetingRoom room = this.getById(id);
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR, "会议室不存在");
        MeetingRoomVO vo = new MeetingRoomVO();
        BeanUtil.copyProperties(room, vo);
        return vo;
    }

    @Override
    public Page<MeetingRoomVO> listRooms(int current, int size, String keyword, Byte status) {
        Page<MeetingRoom> page = new Page<>(current, size);
        LambdaQueryWrapper<MeetingRoom> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(MeetingRoom::getName, keyword)
                    .or()
                    .like(MeetingRoom::getLocation, keyword));
        }
        if (status != null) {
            wrapper.eq(MeetingRoom::getStatus, status);
        }
        wrapper.orderByDesc(MeetingRoom::getCreateTime);
        Page<MeetingRoom> resultPage = this.page(page, wrapper);
        Page<MeetingRoomVO> voPage = new Page<>(current, size, resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(room -> {
            MeetingRoomVO vo = new MeetingRoomVO();
            BeanUtil.copyProperties(room, vo);
            return vo;
        }).toList());
        return voPage;
    }
}
