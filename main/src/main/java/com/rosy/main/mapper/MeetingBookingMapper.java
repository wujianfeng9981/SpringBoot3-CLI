package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.MeetingBooking;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingBookingMapper extends BaseMapper<MeetingBooking> {

    List<MeetingBooking> checkConflict(@Param("roomId") Long roomId,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime,
                                       @Param("excludeId") Long excludeId);
}
