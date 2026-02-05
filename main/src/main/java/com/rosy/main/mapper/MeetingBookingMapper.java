package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.MeetingBooking;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 会议预约表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface MeetingBookingMapper extends BaseMapper<MeetingBooking> {

    /**
     * 检查时间段冲突
     *
     * @param roomId     会议室ID
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param excludeId  排除的预约ID（更新时使用）
     * @return 冲突的预约数量
     */
    @Select("SELECT COUNT(*) FROM meeting_booking " +
            "WHERE room_id = #{roomId} " +
            "AND is_deleted = 0 " +
            "AND status IN (0, 1) " +  // 待审批或已通过
            "AND (#{excludeId} IS NULL OR id != #{excludeId}) " +
            "AND ((start_time <= #{startTime} AND end_time > #{startTime}) " +  // 新预约开始时间在已有预约范围内
            "OR (start_time < #{endTime} AND end_time >= #{endTime}) " +        // 新预约结束时间在已有预约范围内
            "OR (start_time >= #{startTime} AND end_time <= #{endTime}))")      // 已有预约完全包含在新预约范围内
    int checkTimeConflict(@Param("roomId") Long roomId,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime,
                          @Param("excludeId") Long excludeId);

    /**
     * 查询会议室在指定时间范围内的预约列表
     *
     * @param roomId    会议室ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 预约列表
     */
    @Select("SELECT * FROM meeting_booking " +
            "WHERE room_id = #{roomId} " +
            "AND is_deleted = 0 " +
            "AND status IN (0, 1, 4) " +  // 待审批、已通过、已结束
            "AND ((start_time >= #{startTime} AND start_time < #{endTime}) " +
            "OR (end_time > #{startTime} AND end_time <= #{endTime}) " +
            "OR (start_time <= #{startTime} AND end_time >= #{endTime}))")
    List<MeetingBooking> selectByRoomAndTimeRange(@Param("roomId") Long roomId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);
}
