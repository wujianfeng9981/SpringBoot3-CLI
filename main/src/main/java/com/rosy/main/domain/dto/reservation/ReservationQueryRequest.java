package com.rosy.main.domain.dto.reservation;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReservationQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Positive(message = "会议室ID必须为正整数")
    private Long roomId;

    @Positive(message = "申请人ID必须为正整数")
    private Long applicantId;

    @Size(max = 200, message = "会议主题长度不能超过200个字符")
    private String title;

    @Min(value = 0, message = "状态值只能为0、1、2、3或4")
    @Max(value = 4, message = "状态值只能为0、1、2、3或4")
    private Byte status;

    private LocalDateTime startTimeStart;

    private LocalDateTime startTimeEnd;
}
