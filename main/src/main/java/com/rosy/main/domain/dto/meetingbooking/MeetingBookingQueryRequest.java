package com.rosy.main.domain.dto.meetingbooking;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingBookingQueryRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
     * 会议主题（模糊查询）
     */
    private String title;

    /**
     * 状态：0-待审批，1-已通过，2-已驳回，3-已取消，4-已结束
     */
    private Byte status;

    /**
     * 开始时间范围-开始
     */
    private LocalDateTime startTimeBegin;

    /**
     * 开始时间范围-结束
     */
    private LocalDateTime startTimeEnd;
}
