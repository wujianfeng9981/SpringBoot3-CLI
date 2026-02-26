package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.CheckIn;

public interface ICheckInService extends IService<CheckIn> {

    void doCheckIn(Long bookingId, Long userId);

    int getCheckInCount(Long bookingId);
}
