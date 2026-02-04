package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomQueryRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;

public interface IMeetingRoomService extends IService<MeetingRoom> {

    MeetingRoomVO getMeetingRoomVO(MeetingRoom meetingRoom);

    LambdaQueryWrapper<MeetingRoom> getQueryWrapper(MeetingRoomQueryRequest queryRequest);
}
