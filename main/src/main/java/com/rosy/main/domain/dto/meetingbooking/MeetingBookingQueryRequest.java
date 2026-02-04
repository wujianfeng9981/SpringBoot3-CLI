package com.rosy.main.domain.dto.meetingbooking;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class MeetingBookingQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Positive(message = "会议室ID必须为正整数")
    private Long roomId;

    @Positive(message = "用户ID必须为正整数")
    private Long userId;

    @Size(max = 200, message = "会议主题长度不能超过200个字符")
    private String title;

    @Min(value = 0, message = "状态值无效")
    @Max(value = 3, message = "状态值无效")
    private Byte status;

    private LocalDateTime startTimeStart;

    private LocalDateTime startTimeEnd;

    private LocalDateTime endTimeStart;

    private LocalDateTime endTimeEnd;
}
