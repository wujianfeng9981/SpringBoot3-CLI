package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会议预约表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
@TableName("meeting_booking")
public class MeetingBooking implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会议室ID
     */
    private Long roomId;

    /**
     * 预约人ID
     */
    private Long userId;

    /**
     * 会议主题
     */
    private String title;

    /**
     * 会议事由/描述
     */
    private String description;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 参会人数
     */
    private Integer attendees;

    /**
     * 状态：0-待审批，1-已通过，2-已驳回，3-已取消，4-已结束
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;

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
