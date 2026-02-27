package com.rosy.main.modules.meeting.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.rosy.main.modules.meeting.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预约记录实体类
 * 对应数据库表 bookings
 */
@Data
@TableName("bookings")
public class Booking {

    /**
     * 预约ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会议室ID，外键
     */
    @TableField("room_id")
    private Long roomId;

    /**
     * 预约用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 预约用户名称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 预约用户邮箱
     */
    @TableField("user_email")
    private String userEmail;

    /**
     * 会议开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 会议结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 会议主题
     */
    @TableField("subject")
    private String subject;

    /**
     * 会议描述
     */
    @TableField("description")
    private String description;

    /**
     * 参会人数
     */
    @TableField("attendees_count")
    private Integer attendeesCount;

    /**
     * 预约状态，默认为待审批
     */
    @TableField("status")
    private BookingStatus status;

    /**
     * 驳回原因
     */
    @TableField("rejection_reason")
    private String rejectionReason;

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
