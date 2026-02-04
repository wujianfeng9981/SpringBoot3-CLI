package com.rosy.main.domain.dto.meetingcheckin;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MeetingCheckinAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "预约ID不能为空")
    @Positive(message = "预约ID必须为正整数")
    private Long bookingId;

    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    private Long userId;
}
