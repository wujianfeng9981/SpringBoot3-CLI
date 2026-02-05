package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 会议室使用统计视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
public class MeetingRoomStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会议室ID
     */
    private Long roomId;

    /**
     * 会议室名称
     */
    private String roomName;

    /**
     * 预约次数
     */
    private Integer bookingCount;

    /**
     * 实际使用次数（已签到）
     */
    private Integer usedCount;

    /**
     * 总使用时长（分钟）
     */
    private Long totalDuration;

    /**
     * 平均每次使用时长（分钟）
     */
    private Long avgDuration;

    /**
     * 取消次数
     */
    private Integer cancelCount;

    /**
     * 使用率（百分比）
     */
    private Double usageRate;
}
