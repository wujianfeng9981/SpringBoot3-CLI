package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomQueryRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;

public interface IMeetingRoomService extends IService<MeetingRoom> {

    Long addRoom(MeetingRoomAddRequest request);

    Boolean updateRoom(MeetingRoomUpdateRequest request);

    Boolean deleteRoom(Long id);

    MeetingRoomVO getRoomById(Long id);

    List<MeetingRoomVO> getRoomList(MeetingRoomQueryRequest request);

    Page<MeetingRoomVO> getRoomPage(MeetingRoomQueryRequest request, Integer pageNum, Integer pageSize);

    QueryWrapper<MeetingRoom> getQueryWrapper(MeetingRoomQueryRequest request);

    MeetingRoomVO toMeetingRoomVO(MeetingRoom room);
}
