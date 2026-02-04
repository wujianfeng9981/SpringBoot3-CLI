package com.rosy.main.domain.dto.meetingbooking;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MeetingBookingAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "会议室ID不能为空")
    @Positive(message = "会议室ID必须为正整数")
    private Long roomId;

    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    private Long userId;

    @NotBlank(message = "会议主题不能为空")
    @Size(max = 200, message = "会议主题长度不能超过200个字符")
    private String title;

    @Size(max = 1000, message = "事由长度不能超过1000个字符")
    private String description;

    @Min(value = 1, message = "参会人数至少为1")
    private Integer attendees;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
}
