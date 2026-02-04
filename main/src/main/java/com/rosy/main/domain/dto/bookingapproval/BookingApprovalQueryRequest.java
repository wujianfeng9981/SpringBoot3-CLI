package com.rosy.main.domain.dto.bookingapproval;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookingApprovalQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Positive(message = "预约ID必须为正整数")
    private Long bookingId;

    @Positive(message = "审批人ID必须为正整数")
    private Long approverId;

    @Min(value = 0, message = "状态值无效")
    @Max(value = 2, message = "状态值无效")
    private Byte status;
}
