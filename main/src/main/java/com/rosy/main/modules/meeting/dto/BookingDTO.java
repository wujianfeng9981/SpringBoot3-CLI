package com.rosy.main.modules.meeting.dto;

import com.rosy.main.modules.meeting.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预约记录数据传输对象
 * 用于前后端数据交互
 */
@Data
public class BookingDTO {

    /**
     * 预约ID
     */
    private Long id;

    /**
     * 会议室ID
     */
    private Long roomId;

    /**
     * 会议室名称
     */
    private String roomName;

    /**
     * 预约用户ID
     */
    private Long userId;

    /**
     * 预约用户名称
     */
    private String userName;

    /**
     * 预约用户邮箱
     */
    private String userEmail;

    /**
     * 会议开始时间
     */
    private LocalDateTime startTime;

    /**
     * 会议结束时间
     */
    private LocalDateTime endTime;

    /**
     * 会议主题
     */
    private String subject;

    /**
     * 会议描述
     */
    private String description;

    /**
     * 参会人数
     */
    private Integer attendeesCount;

    /**
     * 预约状态
     */
    private BookingStatus status;

    /**
     * 驳回原因
     */
    private String rejectionReason;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
