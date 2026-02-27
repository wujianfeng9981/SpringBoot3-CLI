package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Reservation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationMapper extends BaseMapper<Reservation> {

    @Select("SELECT COUNT(*) FROM reservation " +
            "WHERE room_id = #{roomId} " +
            "AND status = 1 " +
            "AND is_deleted = 0 " +
            "AND ((start_time <= #{startTime} AND end_time > #{startTime}) " +
            "OR (start_time < #{endTime} AND end_time >= #{endTime}) " +
            "OR (start_time >= #{startTime} AND end_time <= #{endTime}))")
    int countConflictReservations(@Param("roomId") Long roomId, 
                                  @Param("startTime") LocalDateTime startTime, 
                                  @Param("endTime") LocalDateTime endTime);

    @Select("SELECT COUNT(*) FROM reservation " +
            "WHERE room_id = #{roomId} " +
            "AND status = 1 " +
            "AND is_deleted = 0 " +
            "AND id != #{excludeId} " +
            "AND ((start_time <= #{startTime} AND end_time > #{startTime}) " +
            "OR (start_time < #{endTime} AND end_time >= #{endTime}) " +
            "OR (start_time >= #{startTime} AND end_time <= #{endTime}))")
    int countConflictReservationsExcludeId(@Param("roomId") Long roomId, 
                                           @Param("startTime") LocalDateTime startTime, 
                                           @Param("endTime") LocalDateTime endTime,
                                           @Param("excludeId") Long excludeId);
}
