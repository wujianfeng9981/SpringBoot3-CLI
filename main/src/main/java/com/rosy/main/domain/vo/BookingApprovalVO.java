package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 预约审批视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
public class BookingApprovalVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 预约ID
     */
    private Long bookingId;

    /**
     * 会议主题
     */
    private String bookingTitle;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批人名称
     */
    private String approverName;

    /**
     * 审批状态：1-通过，2-驳回
     */
    private Byte status;

    /**
     * 审批意见
     */
    private String comment;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
