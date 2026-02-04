package com.rosy.main.domain.dto.meetingbooking;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MeetingBookingUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    @Positive(message = "ID必须为正整数")
    private Long id;

    @Positive(message = "会议室ID必须为正整数")
    private Long roomId;

    @Size(max = 200, message = "会议主题长度不能超过200个字符")
    private String title;

    @Size(max = 1000, message = "事由长度不能超过1000个字符")
    private String description;

    @Min(value = 1, message = "参会人数至少为1")
    private Integer attendees;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Min(value = 0, message = "状态值无效")
    @Max(value = 3, message = "状态值无效")
    private Byte status;
}
