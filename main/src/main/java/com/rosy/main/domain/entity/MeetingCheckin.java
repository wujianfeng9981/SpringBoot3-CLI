package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会议签到表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
@TableName("meeting_checkin")
public class MeetingCheckin implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 预约ID
     */
    private Long bookingId;

    /**
     * 签到人ID
     */
    private Long userId;

    /**
     * 签到时间
     */
    private LocalDateTime checkinTime;

    /**
     * 签到类型：1-正常签到，2-迟到，3-早退
     */
    private Byte checkinType;

    /**
     * 签到地点
     */
    private String location;

    /**
     * 备注
     */
    private String remark;

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
