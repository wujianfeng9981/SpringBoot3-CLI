package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会议签到视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
public class MeetingCheckinVO implements Serializable {

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
     * 签到人ID
     */
    private Long userId;

    /**
     * 签到人名称
     */
    private String userName;

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
}
