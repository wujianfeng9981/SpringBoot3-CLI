package com.rosy.main.modules.meeting.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会议室数据传输对象
 * 用于前后端数据交互
 */
@Data
public class MeetingRoomDTO {

    /**
     * 会议室ID
     */
    private Long id;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 会议室位置
     */
    private String location;

    /**
     * 会议室容量
     */
    private Integer capacity;

    /**
     * 会议室设备清单
     */
    private String equipment;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
