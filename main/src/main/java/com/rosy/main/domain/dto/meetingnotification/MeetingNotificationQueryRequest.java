package com.rosy.main.domain.dto.meetingnotification;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingNotificationQueryRequest extends PageRequest {

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
     * 通知类型：1-审批结果通知，2-会议开始前通知，3-会议取消通知
     */
    private Byte type;

    /**
     * 是否已读：0-未读，1-已读
     */
    private Byte isRead;
}
