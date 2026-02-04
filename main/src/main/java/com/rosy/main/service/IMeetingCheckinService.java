package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinAddRequest;
import com.rosy.main.domain.dto.meetingcheckin.MeetingCheckinQueryRequest;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.vo.MeetingCheckinVO;
import com.rosy.main.domain.vo.MeetingRoomStatisticsVO;

import java.time.LocalDateTime;
import java.util.List;

public interface IMeetingCheckinService extends IService<MeetingCheckin> {

    void checkin(MeetingCheckinAddRequest request);

    MeetingCheckinVO getCheckinVO(MeetingCheckin checkin);

    Page<MeetingCheckinVO> getCheckinVOPage(Page<MeetingCheckin> page);

    LambdaQueryWrapper<MeetingCheckin> getQueryWrapper(MeetingCheckinQueryRequest queryRequest);

    List<MeetingRoomStatisticsVO> getRoomStatistics(LocalDateTime startTime, LocalDateTime endTime);
}
