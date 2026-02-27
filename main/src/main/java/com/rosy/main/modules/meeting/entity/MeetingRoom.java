package com.rosy.main.modules.meeting.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会议室实体类
 * 对应数据库表 meeting_rooms
 */
@Data
@TableName("meeting_rooms")
public class MeetingRoom {

    /**
     * 会议室ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会议室名称，不允许为空，唯一
     */
    @TableField("name")
    private String name;

    /**
     * 会议室位置，不允许为空
     */
    @TableField("location")
    private String location;

    /**
     * 会议室容量，不允许为空
     */
    @TableField("capacity")
    private Integer capacity;

    /**
     * 会议室设备清单
     */
    @TableField("equipment")
    private String equipment;

    /**
     * 是否启用，默认为true
     */
    @TableField("is_active")
    private Boolean isActive;

    /**
     * 创建时间，自动填充
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间，自动填充
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 是否删除，逻辑删除标志
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
