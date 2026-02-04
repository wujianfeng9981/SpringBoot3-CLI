package com.rosy.main.domain.dto.meetingnotification;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class MeetingNotificationQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Positive(message = "预约ID必须为正整数")
    private Long bookingId;

    @Positive(message = "用户ID必须为正整数")
    private Long userId;

    @Min(value = 1, message = "通知类型值无效")
    @Max(value = 2, message = "通知类型值无效")
    private Byte type;

    @Min(value = 0, message = "已读状态值无效")
    @Max(value = 1, message = "已读状态值无效")
    private Byte isRead;
}
