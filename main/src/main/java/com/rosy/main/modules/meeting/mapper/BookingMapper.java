package com.rosy.main.modules.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.modules.meeting.entity.Booking;
import com.rosy.main.modules.meeting.enums.BookingStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约记录数据访问层
 * 继承MyBatis-Plus的BaseMapper，提供基础CRUD操作
 */
@Mapper
public interface BookingMapper extends BaseMapper<Booking> {

    /**
     * 查询用户的所有预约记录，按创建时间倒序
     * @param userId 用户ID
     * @return 预约记录列表
     */
    @Select("SELECT * FROM bookings WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY created_at DESC")
    List<Booking> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询会议室的预约记录
     * @param roomId 会议室ID
     * @param status 预约状态
     * @return 预约记录列表
     */
    @Select("SELECT * FROM bookings WHERE room_id = #{roomId} AND status = #{status} AND is_deleted = 0 ORDER BY start_time ASC")
    List<Booking> selectByRoomIdAndStatus(@Param("roomId") Long roomId, @Param("status") String status);

    /**
     * 查询时间冲突的预约记录
     * 用于检查同一会议室在指定时间段内是否已被预约
     * @param roomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 冲突的预约记录列表
     */
    @Select("SELECT * FROM bookings WHERE room_id = #{roomId} " +
            "AND status IN ('PENDING', 'APPROVED') " +
            "AND is_deleted = 0 " +
            "AND ((start_time <= #{endTime} AND end_time >= #{startTime}))")
    List<Booking> selectConflictingBookings(@Param("roomId") Long roomId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    /**
     * 查询即将开始的会议（用于发送提醒）
     * @param now 当前时间
     * @param reminderTime 提醒时间窗口
     * @return 即将开始的会议列表
     */
    @Select("SELECT * FROM bookings WHERE status = 'APPROVED' " +
            "AND is_deleted = 0 " +
            "AND start_time > #{now} AND start_time <= #{reminderTime}")
    List<Booking> selectUpcomingMeetings(@Param("now") LocalDateTime now,
                                         @Param("reminderTime") LocalDateTime reminderTime);
}
