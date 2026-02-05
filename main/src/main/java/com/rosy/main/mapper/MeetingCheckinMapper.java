package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.vo.MeetingRoomStatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 会议签到表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface MeetingCheckinMapper extends BaseMapper<MeetingCheckin> {

    /**
     * 统计会议室使用情况
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计数据列表
     */
    @Select("SELECT " +
            "    r.id as roomId, " +
            "    r.name as roomName, " +
            "    COUNT(DISTINCT b.id) as bookingCount, " +
            "    COUNT(DISTINCT c.id) as usedCount, " +
            "    COALESCE(SUM(TIMESTAMPDIFF(MINUTE, b.start_time, b.end_time)), 0) as totalDuration " +
            "FROM meeting_room r " +
            "LEFT JOIN meeting_booking b ON r.id = b.room_id " +
            "    AND b.is_deleted = 0 " +
            "    AND b.status IN (1, 4) " +  // 已通过或已结束
            "    AND b.start_time >= #{startTime} " +
            "    AND b.end_time <= #{endTime} " +
            "LEFT JOIN meeting_checkin c ON b.id = c.booking_id " +
            "    AND c.is_deleted = 0 " +
            "WHERE r.is_deleted = 0 " +
            "GROUP BY r.id, r.name " +
            "ORDER BY bookingCount DESC")
    List<MeetingRoomStatisticsVO> selectRoomStatistics(@Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime);
}
