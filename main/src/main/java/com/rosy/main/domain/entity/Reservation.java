package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预约实体类
 * 对应数据库表 reservation
 * 
 * @author Rosy
 * @since 2025-02-27
 */
@Data
@TableName("reservation")
public class Reservation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会议室ID，关联meeting_room表
     */
    private Long roomId;

    /**
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 会议主题
     */
    private String title;

    /**
     * 会议事由/目的
     */
    private String purpose;

    /**
     * 会议开始时间
     */
    private LocalDateTime startTime;

    /**
     * 会议结束时间
     */
    private LocalDateTime endTime;

    /**
     * 预约状态：0-待审批，1-已通过，2-已驳回，3-已取消，4-已完成
     * @see com.rosy.main.domain.enums.ReservationStatus
     */
    private Byte status;

    /**
     * 驳回原因（状态为驳回时有值）
     */
    private String rejectReason;

    /**
     * 参会人数
     */
    private Integer attendeeCount;

    /**
     * 创建者ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Byte version;

    /**
     * 逻辑删除标记：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}
