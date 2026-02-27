package com.rosy.main.modules.meeting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.modules.meeting.dto.MeetingRoomDTO;
import com.rosy.main.modules.meeting.entity.MeetingRoom;
import com.rosy.main.modules.meeting.mapper.MeetingRoomMapper;
import com.rosy.main.modules.meeting.service.IMeetingRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 会议室服务实现类
 * 实现会议室相关的业务逻辑
 */
@Slf4j
@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements IMeetingRoomService {

    @Override
    public MeetingRoomDTO createRoom(MeetingRoomDTO dto) {
        // 检查会议室名称是否已存在
        MeetingRoom existingRoom = baseMapper.selectByName(dto.getName());
        if (existingRoom != null) {
            throw new RuntimeException("会议室名称已存在");
        }

        // 创建会议室实体
        MeetingRoom room = new MeetingRoom();
        BeanUtils.copyProperties(dto, room);
        room.setIsActive(true);

        // 保存到数据库
        baseMapper.insert(room);
        log.info("创建会议室成功: {}", room.getName());

        // 返回DTO
        return convertToDTO(room);
    }

    @Override
    public MeetingRoomDTO getRoomById(Long id) {
        MeetingRoom room = baseMapper.selectById(id);
        if (room == null) {
            throw new RuntimeException("会议室不存在");
        }
        return convertToDTO(room);
    }

    @Override
    public List<MeetingRoomDTO> getAllRooms() {
        List<MeetingRoom> rooms = baseMapper.selectList(null);
        return rooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MeetingRoomDTO> getActiveRooms() {
        List<MeetingRoom> rooms = baseMapper.selectActiveRooms();
        return rooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MeetingRoomDTO> getRoomsByCapacity(Integer minCapacity) {
        List<MeetingRoom> rooms = baseMapper.selectByMinCapacity(minCapacity);
        return rooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MeetingRoomDTO updateRoom(Long id, MeetingRoomDTO dto) {
        // 查询会议室是否存在
        MeetingRoom room = baseMapper.selectById(id);
        if (room == null) {
            throw new RuntimeException("会议室不存在");
        }

        // 如果修改了名称，检查新名称是否已存在
        if (!room.getName().equals(dto.getName())) {
            MeetingRoom existingRoom = baseMapper.selectByName(dto.getName());
            if (existingRoom != null) {
                throw new RuntimeException("会议室名称已存在");
            }
        }

        // 更新属性
        BeanUtils.copyProperties(dto, room);
        room.setId(id);

        // 保存更新
        baseMapper.updateById(room);
        log.info("更新会议室成功: {}", room.getName());

        return convertToDTO(room);
    }

    @Override
    public void deactivateRoom(Long id) {
        MeetingRoom room = baseMapper.selectById(id);
        if (room == null) {
            throw new RuntimeException("会议室不存在");
        }
        room.setIsActive(false);
        baseMapper.updateById(room);
        log.info("停用会议室成功: {}", room.getName());
    }

    @Override
    public void activateRoom(Long id) {
        MeetingRoom room = baseMapper.selectById(id);
        if (room == null) {
            throw new RuntimeException("会议室不存在");
        }
        room.setIsActive(true);
        baseMapper.updateById(room);
        log.info("启用会议室成功: {}", room.getName());
    }

    /**
     * 将实体转换为DTO
     * @param room 会议室实体
     * @return 会议室DTO
     */
    private MeetingRoomDTO convertToDTO(MeetingRoom room) {
        MeetingRoomDTO dto = new MeetingRoomDTO();
        BeanUtils.copyProperties(room, dto);
        return dto;
    }
}
