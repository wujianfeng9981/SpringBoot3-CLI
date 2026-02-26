package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Booking;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingMapper extends BaseMapper<Booking> {

    @Select("SELECT * FROM booking WHERE room_id = #{roomId} AND status IN (0, 1) " +
            "AND ((start_time < #{endTime} AND end_time > #{startTime})) " +
            "AND is_deleted = 0")
    List<Booking> findConflictingBookings(@Param("roomId") Long roomId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
}
