package com.rosy.main.modules.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.modules.meeting.dto.MeetingRoomDTO;
import com.rosy.main.modules.meeting.entity.MeetingRoom;

import java.util.List;

/**
 * 会议室服务接口
 * 定义会议室相关的业务逻辑
 */
public interface IMeetingRoomService extends IService<MeetingRoom> {

    /**
     * 创建会议室
     * @param dto 会议室信息
     * @return 创建的会议室
     */
    MeetingRoomDTO createRoom(MeetingRoomDTO dto);

    /**
     * 根据ID查询会议室
     * @param id 会议室ID
     * @return 会议室信息
     */
    MeetingRoomDTO getRoomById(Long id);

    /**
     * 查询所有会议室
     * @return 会议室列表
     */
    List<MeetingRoomDTO> getAllRooms();

    /**
     * 查询所有启用的会议室
     * @return 启用的会议室列表
     */
    List<MeetingRoomDTO> getActiveRooms();

    /**
     * 根据最小容量查询会议室
     * @param minCapacity 最小容量
     * @return 符合条件的会议室列表
     */
    List<MeetingRoomDTO> getRoomsByCapacity(Integer minCapacity);

    /**
     * 更新会议室信息
     * @param id 会议室ID
     * @param dto 更新的信息
     * @return 更新后的会议室
     */
    MeetingRoomDTO updateRoom(Long id, MeetingRoomDTO dto);

    /**
     * 停用会议室
     * @param id 会议室ID
     */
    void deactivateRoom(Long id);

    /**
     * 启用会议室
     * @param id 会议室ID
     */
    void activateRoom(Long id);
}
