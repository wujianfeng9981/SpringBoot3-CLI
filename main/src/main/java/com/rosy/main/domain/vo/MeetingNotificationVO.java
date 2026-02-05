package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会议通知视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
public class MeetingNotificationVO implements Serializable {

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
     * 接收人ID
     */
    private Long userId;

    /**
     * 接收人名称
     */
    private String userName;

    /**
     * 通知类型：1-审批结果通知，2-会议开始前通知，3-会议取消通知
     */
    private Byte type;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 是否已读：0-未读，1-已读
     */
    private Byte isRead;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
