package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;

public interface IMeetingRoomService extends IService<MeetingRoom> {

    Long addRoom(MeetingRoomAddRequest request);

    void updateRoom(MeetingRoomUpdateRequest request);

    void deleteRoom(Long id);

    MeetingRoomVO getRoomById(Long id);

    Page<MeetingRoomVO> listRooms(int current, int size, String keyword, Byte status);
}
