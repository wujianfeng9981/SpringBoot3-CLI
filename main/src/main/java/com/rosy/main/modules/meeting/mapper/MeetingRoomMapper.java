package com.rosy.main.modules.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.modules.meeting.entity.MeetingRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 会议室数据访问层
 * 继承MyBatis-Plus的BaseMapper，提供基础CRUD操作
 */
@Mapper
public interface MeetingRoomMapper extends BaseMapper<MeetingRoom> {

    /**
     * 查询所有启用的会议室
     * @return 启用的会议室列表
     */
    @Select("SELECT * FROM meeting_rooms WHERE is_active = 1 AND is_deleted = 0")
    List<MeetingRoom> selectActiveRooms();

    /**
     * 根据最小容量查询会议室
     * @param capacity 最小容量
     * @return 符合条件的会议室列表
     */
    @Select("SELECT * FROM meeting_rooms WHERE capacity >= #{capacity} AND is_active = 1 AND is_deleted = 0")
    List<MeetingRoom> selectByMinCapacity(@Param("capacity") Integer capacity);

    /**
     * 根据名称查询会议室
     * @param name 会议室名称
     * @return 会议室对象
     */
    @Select("SELECT * FROM meeting_rooms WHERE name = #{name} AND is_deleted = 0 LIMIT 1")
    MeetingRoom selectByName(@Param("name") String name);
}
