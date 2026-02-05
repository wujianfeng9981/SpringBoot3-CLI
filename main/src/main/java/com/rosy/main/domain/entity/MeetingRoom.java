package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会议室表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
@TableName("meeting_room")
public class MeetingRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 位置
     */
    private String location;

    /**
     * 容量（人数）
     */
    private Integer capacity;

    /**
     * 设备（投影仪/白板/音响等，JSON格式）
     */
    private String equipment;

    /**
     * 状态：0-禁用，1-可用
     */
    private Byte status;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Byte isDeleted;
}
