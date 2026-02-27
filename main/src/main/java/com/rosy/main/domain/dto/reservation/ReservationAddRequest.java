package com.rosy.main.domain.dto.reservation;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ReservationAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "会议室ID不能为空")
    private Long roomId;

    @NotNull(message = "申请人ID不能为空")
    private Long applicantId;

    @NotBlank(message = "会议主题不能为空")
    @Size(max = 200, message = "会议主题长度不能超过200个字符")
    private String title;

    @NotBlank(message = "会议事由不能为空")
    @Size(max = 500, message = "会议事由长度不能超过500个字符")
    private String purpose;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @NotNull(message = "参会人数不能为空")
    @Min(value = 1, message = "参会人数不能小于1")
    private Integer attendeeCount;
}
