package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.MeetingNotification;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface MeetingNotificationMapper extends BaseMapper<MeetingNotification> {

    @Update("UPDATE meeting_notification SET is_read = 1, read_time = NOW() WHERE user_id = #{userId} AND is_deleted = 0")
    int markAllAsRead(@Param("userId") Long userId);
}
