package com.rosy.main.domain.dto.meetingcheckin;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingCheckinQueryRequest extends PageRequest {

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
     * 签到人ID
     */
    private Long userId;

    /**
     * 签到类型：1-正常签到，2-迟到，3-早退
     */
    private Byte checkinType;

    /**
     * 签到时间范围-开始
     */
    private LocalDateTime checkinTimeBegin;

    /**
     * 签到时间范围-结束
     */
    private LocalDateTime checkinTimeEnd;
}
