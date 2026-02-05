package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.vo.MeetingCheckinVO;

public interface IMeetingCheckinService extends IService<MeetingCheckin> {

    void checkin(Long bookingId, Long userId, String userName);

    Page<MeetingCheckinVO> listCheckins(int current, int size, Long bookingId, Long userId);

    int countCheckins(Long bookingId);
}
